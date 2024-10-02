package com.mentoapp.mentoapp.Repository;

import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.categories c WHERE c.id IN:categoryIds")
    List<User> findByCategoryIds(List<Long> categoryIds);

    @Query("SELECT u FROM User u LEFT JOIN u.categories c JOIN u.roles r WHERE c.id IN:categoryIds AND r = :role"
            + " GROUP BY u HAVING COUNT(c) = :categoryIdsListSize")
    Page<User> findByCategoryIdsAndRole(Set<Long> categoryIds, long categoryIdsListSize, UserRole role, Pageable page);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    Page<User> findByRole(UserRole role, Pageable page);

    @Query("SELECT u FROM User u LEFT JOIN u.categories c JOIN u.roles r WHERE c.id IN:categoryIds AND r = :role"
            + " GROUP BY u HAVING COUNT(c) = :categoryIdsListSize")
    List<User> findByCategoryIdsAndRole(Set<Long> categoryIds,long categoryIdsListSize, UserRole role);

    List<User> findByRolesContaining(UserRole role);
}
