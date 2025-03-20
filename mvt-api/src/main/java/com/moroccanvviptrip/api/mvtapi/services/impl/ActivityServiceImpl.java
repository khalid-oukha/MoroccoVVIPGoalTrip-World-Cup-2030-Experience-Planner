package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.repository.ActivityRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.CategoryService;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.utils.specifications.ActivitySpecifications;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ActivityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final ActivityMapper activityMapper;
    private final ActivitySpecifications activitySpecifications;
    private final FileStorageService fileStorageService;
    private final String BASE_FILE_URL = "http://localhost:8080/api/v1/files/";

    @Override
    public List<Activity> findTopActivities(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<Activity> spec = Specification.where(activitySpecifications.isAvailable(true));
        return activityRepository.findAll(spec, pageable).getContent();
    }

    @Override
    public Activity findById(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
    }

    @Override
    public Page<Activity> findAll(Long cityId, Long categoryId, Boolean available, String search, Pageable pageable) {
        Specification<Activity> spec = Specification
                .where(activitySpecifications.hasCity(cityId))
                .and(activitySpecifications.hasCategory(categoryId))
                .and(activitySpecifications.isAvailable(available))
                .and(activitySpecifications.containsKeyword(search));

        return activityRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public Activity create(ActivityRequestDto activityRequestDto) {
        if (activityRequestDto.getImageUri() == null || activityRequestDto.getImageUri().isEmpty()) {
            throw new IllegalArgumentException("Image file is required.");
        }

        Activity activity = activityMapper.toEntity(activityRequestDto);

        activity.setCategory(categoryService.findById(activityRequestDto.getCategoryId()));
        activity.setCity(cityService.findById(activityRequestDto.getCityId()));

        String imageUrl = BASE_FILE_URL + activity.getImageUri();
        activity.setImageUri(imageUrl);

        activity.setAvailable(true);

        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public Activity update(UUID id, ActivityUpdateDto activityUpdateDto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        handleImageUpdate(activity, activityUpdateDto.getImageUri());

        activityMapper.partialUpdate(activityUpdateDto, activity);

        if (activityUpdateDto.getImageUri() != null && !activityUpdateDto.getImageUri().isEmpty()) {
            String imageUrl = BASE_FILE_URL + activity.getImageUri();
            activity.setImageUri(imageUrl);
        }

        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        if (activity.getImageUri() != null && !activity.getImageUri().isEmpty()) {
            String fileName = activity.getImageUri().substring(activity.getImageUri().lastIndexOf("/") + 1);
            fileStorageService.delete(fileName);
        }

        activityRepository.delete(activity);
    }

    /**
     * Helper method to handle image updates
     */
    private void handleImageUpdate(Activity activity, MultipartFile newImage) {
        if (newImage == null || newImage.isEmpty()) {
            return;
        }

        if (activity.getImageUri() != null && !activity.getImageUri().isEmpty()) {
            String oldFileName = activity.getImageUri().substring(activity.getImageUri().lastIndexOf("/") + 1);
            fileStorageService.delete(oldFileName);
        }
    }
}