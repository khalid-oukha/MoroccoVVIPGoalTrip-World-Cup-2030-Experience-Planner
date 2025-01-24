package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ActivityService {
    Activity findById(UUID id);

    List<Activity> findAll();

    Activity create(ActivityRequestDto activityRequestDto);

    Activity update(UUID id, ActivityUpdateDto activityUpdateDto);

    void delete(UUID id);

}
