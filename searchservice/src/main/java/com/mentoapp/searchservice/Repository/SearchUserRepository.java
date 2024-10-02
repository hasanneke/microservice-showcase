package com.mentoapp.searchservice.Repository;

import com.mentoapp.searchservice.Entity.SearchUserObject;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SearchUserRepository extends CrudRepository<SearchUserObject,Long> {
    List<SearchHit<SearchUserObject>> findByNameContainingOrDescriptionContainingIgnoreCase(String name, String description);
}
