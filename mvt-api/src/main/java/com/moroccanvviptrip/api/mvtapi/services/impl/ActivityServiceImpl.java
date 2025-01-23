package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.repository.ActivityRepository;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;

    @Override
    public Activity findById(Long id) {
        return null;
    }

    @Override
    public List<Activity> findAll() {
        return List.of();
    }

    @Override
    public Activity create(Activity activity) {
        return null;
    }

    @Override
    public Activity update(Activity activity) {
        return null;
    }

    @Override
    public Activity delete(Long id) {
        return null;
    }
}
