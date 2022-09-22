package com.oma.productmanagementsystem.repositories;

import com.oma.productmanagementsystem.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query(
            value = "SELECT u FROM UserEntity u WHERE u.email = ?1")
    UserEntity findByEmail(String username);
}
