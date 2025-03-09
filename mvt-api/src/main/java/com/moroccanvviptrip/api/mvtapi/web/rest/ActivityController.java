package com.moroccanvviptrip.api.mvtapi.web.rest;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.services.ActivityService;
import com.moroccanvviptrip.api.mvtapi.web.VM.ActivityResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.Activity.ActivityUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.ActivityMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Page<ActivityResponseVm>> findAll(
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Activity> activities = activityService.findAll(cityId, categoryId, available, search, pageable);
        Page<ActivityResponseVm> response = activities.map(activityMapper::toResponseVm);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseVm> findById(@PathVariable UUID id) {
        Activity activity = activityService.findById(id);
        ActivityResponseVm response = activityMapper.toResponseVm(activity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActivityResponseVm> create(
            @Valid @ModelAttribute ActivityRequestDto activityRequestDto) {
        Activity activity = activityService.create(activityRequestDto);
        ActivityResponseVm response = activityMapper.toResponseVm(activity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/top")
    public ResponseEntity<List<ActivityResponseVm>> findTopActivities(
            @RequestParam(defaultValue = "4") int limit) {
        List<Activity> topActivities = activityService.findTopActivities(limit);
        List<ActivityResponseVm> response = topActivities.stream()
                .map(activityMapper::toResponseVm)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActivityResponseVm> update(
            @PathVariable UUID id,
            @Valid @ModelAttribute ActivityUpdateDto activityUpdateDto) {
        Activity activity = activityService.update(id, activityUpdateDto);
        ActivityResponseVm response = activityMapper.toResponseVm(activity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
