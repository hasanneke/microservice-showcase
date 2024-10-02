package com.mentoapp.searchservice.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {
    private long id;
    private String name;
    private int level;
    private Category parent;
}
