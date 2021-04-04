package com.medical.taskdemo.domain.repository;

import com.medical.taskdemo.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByIdAndParent_Id(String productId, String parentId);
    List<Company> findAllByParent_Id(String parentId);

}
