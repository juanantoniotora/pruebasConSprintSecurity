package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    @Query("SELECT u FROM UserEntity u WHERE u.username = ?1")
    Optional<UserEntity> getName(String username);

}
