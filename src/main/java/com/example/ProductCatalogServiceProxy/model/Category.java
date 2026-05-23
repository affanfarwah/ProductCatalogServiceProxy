package com.example.ProductCatalogServiceProxy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.websocket.OnClose;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModel{
    private String name;
    private String description;
//    @OneToMany //Default is: //FetchType.LAZY: means products load only when accessed.
//@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
//@Fetch(FetchMode.SELECT)
//    @BatchSize(size=3)   //5<10
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
