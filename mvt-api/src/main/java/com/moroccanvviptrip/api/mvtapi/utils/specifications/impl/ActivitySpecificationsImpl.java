package com.moroccanvviptrip.api.mvtapi.utils.specifications.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Activity;
import com.moroccanvviptrip.api.mvtapi.utils.specifications.ActivitySpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ActivitySpecificationsImpl implements ActivitySpecifications {
    @Override
    public Specification<Activity> hasCity(Long cityId) {
        return ((root, query, criteriaBuilder) -> cityId == null ? null
                : criteriaBuilder.equal(root.get("city").get("id"), cityId));
    }

    @Override
    public Specification<Activity> hasCategory(Long categoryId) {
        return ((root, query, criteriaBuilder) -> categoryId == null ? null
                : criteriaBuilder.equal(root.get("category").get("id"), categoryId));

    }

    @Override
    public Specification<Activity> isAvailable(Boolean available) {
        return ((root, query, criteriaBuilder) -> available == null ? null
                : criteriaBuilder.equal(root.get("available"), available));
    }

    @Override
    public Specification<Activity> containsKeyword(String keyword) {
        return (root, query, cb) -> keyword == null || keyword.isEmpty() ? null : cb.or(
                cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
        );
    }
}
