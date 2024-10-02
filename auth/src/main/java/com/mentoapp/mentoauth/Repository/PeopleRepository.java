package com.mentoapp.mentoauth.Repository;

import com.mentoapp.mentoauth.Entity.People;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends CrudRepository<People, Long> {
    Optional<People> findByEmail(String email);
    Optional<People> findByEmailAndPassword(String email, String password);
}
