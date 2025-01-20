package com.moroccanvviptrip.api.mvtapi.repository;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
