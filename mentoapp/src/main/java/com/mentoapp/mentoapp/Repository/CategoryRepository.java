package com.mentoapp.mentoapp.Repository;

import com.mentoapp.mentoapp.Entity.Category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByLevelAndParentId(int level, long parentId);
    List<Category> findByLevel(Integer level);
    List<Category> findByParentId(Long id);
    List<Category> findByName(String name);
}
