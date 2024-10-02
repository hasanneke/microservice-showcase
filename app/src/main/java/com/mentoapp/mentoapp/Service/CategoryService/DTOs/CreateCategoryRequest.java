package com.mentoapp.mentoapp.Service.CategoryService.DTOs;

import com.mentoapp.mentoapp.Entity.Category.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateCategoryRequest {
    @NotBlank
    private String name;
    private Long parent;
    @Min(0)
    @Max(2)
    private int level;
}
