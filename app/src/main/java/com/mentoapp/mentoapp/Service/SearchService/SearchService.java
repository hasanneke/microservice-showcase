package com.mentoapp.mentoapp.Service.SearchService;

import com.mentoapp.mentoapp.Entity.User.User;
import com.mentoapp.mentoapp.Repository.UserRepository;
import com.mentoapp.mentoapp.Entity.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class SearchService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> searchUsers(Set<Long> categoryIds, Pageable pageable) {
       if (categoryIds == null || categoryIds.isEmpty()){
           return userRepository.findByRole(UserRole.mentor, pageable);
       }else{
           return userRepository.findByCategoryIdsAndRole(categoryIds, categoryIds.size(), UserRole.mentor, pageable);
       }
    }
}
