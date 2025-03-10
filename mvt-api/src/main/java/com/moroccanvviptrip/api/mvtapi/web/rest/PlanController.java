package com.moroccanvviptrip.api.mvtapi.web.rest;

import com.moroccanvviptrip.api.mvtapi.domain.Plan;
import com.moroccanvviptrip.api.mvtapi.services.PlanService;
import com.moroccanvviptrip.api.mvtapi.web.VM.PlanResponseVm.PlanResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.PlannedActivities.PlannedActivityRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.plan.PlanUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.PlanMapper;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final PlanMapper planMapper;

    @GetMapping
    public ResponseEntity<Page<PlanResponseVm>> getAllPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Plan> plans = planService.findAll(pageable);
        Page<PlanResponseVm> response = plans.map(planMapper::toResponseVm);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-plans")
    public ResponseEntity<Page<PlanResponseVm>> getMyPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Plan> plans = planService.findUserPlans(pageable);
        Page<PlanResponseVm> response = plans.map(planMapper::toResponseVm);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseVm> getPlanById(@PathVariable UUID id) {
        Plan plan = planService.findById(id);
        PlanResponseVm response = planMapper.toResponseVm(plan);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlanResponseVm> createPlan(
            @Valid @ModelAttribute PlanRequestDto planRequestDto) {
        Plan createdPlan = planService.create(planRequestDto);
        PlanResponseVm response = planMapper.toResponseVm(createdPlan);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlanResponseVm> updatePlan(
            @PathVariable UUID id,
            @Valid @ModelAttribute PlanUpdateDto planUpdateDto) {

        Plan updatedPlan = planService.update(id, planUpdateDto);
        PlanResponseVm response = planMapper.toResponseVm(updatedPlan);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/activities/{activityId}")
    public ResponseEntity<Void> addActivityToPlan(
            @PathVariable UUID planId,
            @PathVariable UUID activityId,
            @Valid @RequestBody PlannedActivityRequestDto requestDto) {

        planService.addActivityToPlan(planId, activityId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{planId}/activities/{activityId}")
    public ResponseEntity<Void> removeActivityFromPlan(
            @PathVariable UUID planId,
            @PathVariable UUID activityId) {

        planService.removeActivityFromPlan(planId, activityId);
        return ResponseEntity.noContent().build();
    }
}