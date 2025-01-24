package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.domain.City;
import com.moroccanvviptrip.api.mvtapi.repository.ActivityRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.services.CategoryService;
import com.moroccanvviptrip.api.mvtapi.services.CityService;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ActivityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final ActivityMapper activityMapper;

    @Override
    public Activity findById(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Activity create(ActivityRequestDto activityRequestDto) {
        Category category = categoryService.findById(activityRequestDto.getCategoryId());
        City city = cityService.findById(activityRequestDto.getCityId());

        Activity activity = activityMapper.toEntity(activityRequestDto);

        activity.setCategory(category);
        activity.setCity(city);
        activity.setAvailable(true);
        return activityRepository.save(activity);
    }

    @Override
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

        return activityRepository.save(existingActivity);
    }

    @Override
    public void delete(UUID id) {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

        activityRepository.delete(existingActivity);
    }
}
