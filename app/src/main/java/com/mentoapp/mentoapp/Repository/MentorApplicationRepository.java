package com.mentoapp.mentoapp.Repository;

import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.MentorApplication.MentorApplication;
import com.mentoapp.mentoapp.Entity.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MentorApplicationRepository extends JpaRepository<MentorApplication, Long> {
    @Query("Select cat from Category cat where cat.id in :ids" )
    Set<Category> findByIds(@Param("ids") Set<Long> categoryIds);
    Optional<List<MentorApplication>> findByApplicant(User user);
    Page<MentorApplication> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
