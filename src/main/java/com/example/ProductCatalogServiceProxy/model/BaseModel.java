package com.example.ProductCatalogServiceProxy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public abstract class BaseModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Status status;
}
