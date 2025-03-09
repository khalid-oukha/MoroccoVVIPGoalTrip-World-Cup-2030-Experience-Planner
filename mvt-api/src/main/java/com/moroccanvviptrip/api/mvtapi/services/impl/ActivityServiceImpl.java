package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.domain.City;
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


    @Override
    public List<Activity> findTopActivities(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Activity> spec = Specification
                .where(activitySpecifications.isAvailable(true));

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
        MultipartFile imageFile = activityRequestDto.getImageUri();
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required.");
        }

        Category category = categoryService.findById(activityRequestDto.getCategoryId());
        City city = cityService.findById(activityRequestDto.getCityId());

        Activity activity = activityMapper.toEntity(activityRequestDto);

        String fileName = fileStorageService.store(imageFile);
        String imageUrl = "http://localhost:8080/api/v1/files/" + fileName;
        activity.setImageUri(imageUrl);

        activity.setCategory(category);
        activity.setCity(city);
        activity.setAvailable(true);

        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public Activity update(UUID id, ActivityUpdateDto activityUpdateDto) {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        activityMapper.partialUpdate(activityUpdateDto, existingActivity);

        if (activityUpdateDto.getCategoryId() != null) {
            Category category = categoryService.findById(activityUpdateDto.getCategoryId());
            existingActivity.setCategory(category);
        }

        if (activityUpdateDto.getCityId() != null) {
            City city = cityService.findById(activityUpdateDto.getCityId());
            existingActivity.setCity(city);
        }

        MultipartFile newImageFile = activityUpdateDto.getImageUri();
        if (newImageFile != null && !newImageFile.isEmpty()) {
            if (existingActivity.getImageUri() != null && !existingActivity.getImageUri().isEmpty()) {
                String oldFileName = existingActivity.getImageUri()
                        .substring(existingActivity.getImageUri().lastIndexOf("/") + 1);
                fileStorageService.delete(oldFileName);
            }

            String newFileName = fileStorageService.store(newImageFile);
            String newImageUrl = "http://localhost:8080/api/v1/files/" + newFileName;
            existingActivity.setImageUri(newImageUrl);
        }

        return activityRepository.save(existingActivity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        if (existingActivity.getImageUri() != null && !existingActivity.getImageUri().isEmpty()) {
            String fileName = existingActivity.getImageUri()
                    .substring(existingActivity.getImageUri().lastIndexOf("/") + 1);
            fileStorageService.delete(fileName);
        }

        activityRepository.delete(existingActivity);
    }
}
