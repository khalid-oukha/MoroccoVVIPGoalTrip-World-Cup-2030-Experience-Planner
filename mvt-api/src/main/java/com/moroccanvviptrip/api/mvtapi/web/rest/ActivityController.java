package com.moroccanvviptrip.api.mvtapi.web.rest;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.web.VM.ActivityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ActivityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @GetMapping
    public ResponseEntity<List<ActivityResponseVm>> findAll() {
        List<Activity> activities = activityService.findAll();
        List<ActivityResponseVm> response = activities.stream()
                .map(activityMapper::toResponseVm)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseVm> findById(@PathVariable UUID id) {
        Activity activity = activityService.findById(id);
        ActivityResponseVm response = activityMapper.toResponseVm(activity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ActivityResponseVm> create(@RequestBody ActivityRequestDto activityRequestDto) {
        Activity activity = activityService.create(activityRequestDto);
        ActivityResponseVm response = activityMapper.toResponseVm(activity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponseVm> update(@PathVariable UUID id, @RequestBody ActivityUpdateDto activityUpdateDto) {
        Activity activity = activityService.update(id, activityUpdateDto);
        ActivityResponseVm response = activityMapper.toResponseVm(activity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
