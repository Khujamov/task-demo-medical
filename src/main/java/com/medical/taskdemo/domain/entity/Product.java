package com.medical.taskdemo.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {

    @Id
    private String id;

    private String name;

    private String docDate;

    @Column(name = "parent_id")
    private String parentId;

    public Product(String productId, String name, String docDate, String parentId) {
        this.id = productId;
        this.name = name;
        this.docDate = docDate;
        this.parentId = parentId;
    }
}
