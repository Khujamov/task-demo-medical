package com.medical.taskdemo.domain.repository;

import com.medical.taskdemo.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByIdAndParentId(String productId, String parentId);
    List<Product> findAllByParentId(String id);
}
