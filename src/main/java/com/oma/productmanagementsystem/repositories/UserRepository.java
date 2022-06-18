package com.oma.productmanagementsystem.repositories;

import com.oma.productmanagementsystem.entities.ProductUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ProductUser, String> {
}
