package com.nuamx.customers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String name;

    @Setter
    private String email;

}
