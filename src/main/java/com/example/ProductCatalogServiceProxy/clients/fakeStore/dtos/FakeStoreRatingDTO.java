package com.example.ProductCatalogServiceProxy.clients.fakeStore.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FakeStoreRatingDTO {
    private Double rate;
    private Long count;
}