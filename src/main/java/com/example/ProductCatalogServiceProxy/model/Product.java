package com.example.ProductCatalogServiceProxy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

@Getter
@Setter
@Entity
public class Product extends BaseModel{
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    //    @ManyToOne //Default is: //FetchType.EAGER: means category loads immediately with product.
    @ManyToOne(cascade = CascadeType.ALL)  // create the category object as well bcz of cascade
    private Category category;
    private Boolean isSpecial;
}
