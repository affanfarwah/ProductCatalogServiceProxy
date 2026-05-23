package com.example.ProductCatalogServiceProxy.clients.fakeStore.dto;

import com.example.ProductCatalogServiceProxy.dto.RatingDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FakeStoreProductDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String image;
    private String category;
    private RatingDTO ratingDTO;
}
