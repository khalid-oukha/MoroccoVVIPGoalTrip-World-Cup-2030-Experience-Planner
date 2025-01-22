package com.moroccanvviptrip.api.mvtapi.services;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryUpdateDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category create(CategoryRequestDto categoryRequestDto);

    void delete(Long id);

    @Transactional
    Category update(Long id, CategoryUpdateDto categoryUpdateDto);
}
