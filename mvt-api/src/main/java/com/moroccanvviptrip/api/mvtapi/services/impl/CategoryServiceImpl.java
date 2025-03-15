package com.moroccanvviptrip.api.mvtapi.services.impl;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.repository.CategoryRepository;
import com.moroccanvviptrip.api.mvtapi.services.CategoryService;
import com.moroccanvviptrip.api.mvtapi.utils.FileStorageService;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryUpdateDto;
import com.moroccanvviptrip.api.mvtapi.web.exception.PropertyExistsException;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileStorageService fileStorageService;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));
    }

    @Override
    @Transactional
    public Category create(CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new PropertyExistsException("Category with this name already exists.");
        }

        if (categoryRequestDto.getImageUri() == null || categoryRequestDto.getImageUri().isEmpty()) {
            throw new IllegalArgumentException("Image file is required.");
        }

        String fileName = fileStorageService.store(categoryRequestDto.getImageUri());

        String imageUrl = "http://localhost:8080/api/v1/files/" + fileName;

        Category category = categoryMapper.toEntity(categoryRequestDto);
        category.setImageUri(imageUrl);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        if (category.getImageUri() != null && !category.getImageUri().isEmpty()) {
            String fileName = category.getImageUri()
                    .substring(category.getImageUri().lastIndexOf("/") + 1);
            fileStorageService.delete(fileName);
        }

        categoryRepository.delete(category);
    }


    @Transactional
    @Override
    public Category update(Long id, CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        if (categoryUpdateDto.getDescription() != null && !categoryUpdateDto.getDescription().isEmpty()) {
            category.setDescription(categoryUpdateDto.getDescription());
        }

        if (categoryUpdateDto.getImageUri() != null && !categoryUpdateDto.getImageUri().isEmpty()) {
            if (category.getImageUri() != null && !category.getImageUri().isEmpty()) {
                String oldFileName = category.getImageUri()
                        .substring(category.getImageUri().lastIndexOf("/") + 1);
                fileStorageService.delete(oldFileName);
            }

            String fileName = fileStorageService.store(categoryUpdateDto.getImageUri());
            String imageUrl = "http://localhost:8080/api/v1/files/" + fileName;
            category.setImageUri(imageUrl);
        }

        return categoryRepository.save(category);
    }
}