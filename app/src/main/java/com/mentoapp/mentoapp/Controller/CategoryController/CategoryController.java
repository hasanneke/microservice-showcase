package com.mentoapp.mentoapp.Controller.CategoryController;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Service.CategoryService.CategoryService;
import com.mentoapp.mentoapp.Service.CategoryService.DTOs.CreateCategoryRequest;
import com.mentoapp.mentoapp.Service.CategoryService.DTOs.UpdateCategoryRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/categories")
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody @Validated CreateCategoryRequest createCategoryRequest) throws BadRequestException {
        return categoryService.createCategory(createCategoryRequest);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@PathVariable(name = "id") Long id, @RequestBody @Validated UpdateCategoryRequest updateCategoryRequest) {
        return categoryService.updateCategory(id, updateCategoryRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.OK)
    public Category deleteCategory(@PathVariable(name = "id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategories(@RequestParam(name = "level", required = false) Integer level,
                                        @RequestParam(name = "parent", required = false) Long parentId) {
        return categoryService.getCategories(level, parentId);
    }
}
