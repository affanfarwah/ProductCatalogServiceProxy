package com.example.ProductCatalogServiceProxy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; /*  A class that represents an immutable universally unique identifier (UUID).
                            A UUID represents a 128-bit value. */
    private Date createdAt;
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;
}
