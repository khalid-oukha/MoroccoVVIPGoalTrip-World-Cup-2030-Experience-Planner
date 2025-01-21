package com.moroccanvviptrip.api.mvtapi.web.rest;

import com.moroccanvviptrip.api.mvtapi.domain.Category;
import com.moroccanvviptrip.api.mvtapi.services.CategoryService;
import com.moroccanvviptrip.api.mvtapi.web.VM.CategoryResponseVm;
import com.moroccanvviptrip.api.mvtapi.web.dto.category.CategoryRequestDto;
import com.moroccanvviptrip.api.mvtapi.web.mapper.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseVm>> getAll() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponseVm> responseVmList = categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseVmList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseVm> getById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseVm> create(@Valid @ModelAttribute CategoryRequestDto categoryRequestDto) {
        Category createdCategory = categoryService.create(categoryRequestDto);
        CategoryResponseVm responseVm = categoryMapper.toResponse(createdCategory);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + createdCategory.getId())).body(responseVm);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}