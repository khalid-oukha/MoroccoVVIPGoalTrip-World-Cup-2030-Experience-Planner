package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;

import java.util.List;

public interface ActivityService {
    Activity findById(Long id);

    List<Activity> findAll();

    Activity create(Activity activity);

    Activity update(Activity activity);

    Activity delete(Long id);

}
