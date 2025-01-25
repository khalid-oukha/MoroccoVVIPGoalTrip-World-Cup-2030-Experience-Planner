package com.moroccanvviptrip.api.mvtapi.utils.specifications;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import org.springframework.data.jpa.domain.Specification;

public interface ActivitySpecifications {
    Specification<Activity> hasCity(Long cityId);

    Specification<Activity> hasCategory(Long categoryId);

    Specification<Activity> isAvailable(Boolean available);

    Specification<Activity> containsKeyword(String keyword);
}
