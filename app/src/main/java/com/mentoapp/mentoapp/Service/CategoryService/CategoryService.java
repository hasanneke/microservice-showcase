package com.mentoapp.mentoapp.Service.CategoryService;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Exception.Instance.ResourceNotFound;
import com.mentoapp.mentoapp.Repository.CategoryRepository;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Service.CategoryService.DTOs.CreateCategoryRequest;
import com.mentoapp.mentoapp.Service.CategoryService.DTOs.UpdateCategoryRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryRequest createCategoryRequest) throws BadRequestException {
        Category parent = null;
        if (createCategoryRequest.getParent() != null && createCategoryRequest.getLevel() > 0) {
            parent =
                    categoryRepository.findById(createCategoryRequest.getParent()).orElseThrow(() -> new ResourceNotFound("Parent category is not found"));
        }

        List<Category> checkSameCategory =
                categoryRepository.findByName(createCategoryRequest.getName());

        if (!checkSameCategory.isEmpty()){
            throw new BadRequestException("Category with same name is already exist");
        }

        return categoryRepository.save(Category.builder().level(createCategoryRequest.getLevel()).name(createCategoryRequest.getName()).parent(parent).build());
    }

    public Category updateCategory(Long id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Category not found"));

        category.setName(updateCategoryRequest.getName());
        category.setLevel(updateCategoryRequest.getLevel());
        if(updateCategoryRequest.getParent() != null){
            Category parentCategory = categoryRepository.findById(updateCategoryRequest.getParent()).orElse(null);
            category.setParent(parentCategory);
        }

        return categoryRepository.save(category);
    }

    public Category deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Category not found"));

        if(category.getLevel() == 0){
            List<Category> children = categoryRepository.findByParentId(category.getId());
            children.forEach((element)->{
                categoryRepository.delete(element);
            });
        }

        categoryRepository.delete(category);
        return category;
    }

    public List<Category> getCategories(Integer level, Long parentId) {
        if (level != null && parentId != null){
            return categoryRepository.findByLevelAndParentId(level, parentId);
        }
        if (level != null){
            return categoryRepository.findByLevel(level);
        }
        if (parentId != null) {
            return categoryRepository.findByParentId(parentId);
        }
        return  categoryRepository.findAll();
    }
}
