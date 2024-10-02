package com.mentoapp.searchservice.Entity;

import com.mentoapp.searchservice.Domain.Category;
import com.mentoapp.searchservice.Domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.HashSet;
import java.util.Set;

@Document(indexName = "users", createIndex = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserObject {
    @Id
    private long id;
    private String name;
    private String surname;
    private String fullName;
    private String email;
    private Set<UserRole> roles = new HashSet<>();
    private String urlAvatar;
    private Set<Category> categories = new HashSet<>();
    private String description;
}
