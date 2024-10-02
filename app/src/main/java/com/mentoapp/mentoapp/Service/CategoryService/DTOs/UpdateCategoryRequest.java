package com.mentoapp.mentoapp.Service.CategoryService.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateCategoryRequest {
    @NotBlank
    private String name;
    private Long parent;
    @Min(0)
    @Max(2)
    private int level;
}
