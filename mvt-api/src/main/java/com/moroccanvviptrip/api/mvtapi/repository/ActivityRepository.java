package com.moroccanvviptrip.api.mvtapi.repository;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID>, JpaSpecificationExecutor<Activity> {
    boolean existsById(UUID id);
}
