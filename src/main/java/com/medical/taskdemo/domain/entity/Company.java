package com.medical.taskdemo.domain.entity;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company {
    @Id
    private String id;

    private String name;

    private String docDate;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Company parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Company> children = new HashSet<>();

    public Company(String productId, String name, String docDate) {
        this.id = productId;
        this.name = name;
        this.docDate = docDate;
    }

    public Company(String productId, String name, String docDate, Company parent) {
        this.id = productId;
        this.name = name;
        this.docDate = docDate;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id.equals(company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
