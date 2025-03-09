package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ActivityService {
    Activity findById(UUID id);


    Page<Activity> findAll(Long cityId, Long categoryId, Boolean available, String search, Pageable pageable);

    Activity create(ActivityRequestDto activityRequestDto);

    Activity update(UUID id, ActivityUpdateDto activityUpdateDto);

    void delete(UUID id);

    List<Activity> findTopActivities(int limit);

}
