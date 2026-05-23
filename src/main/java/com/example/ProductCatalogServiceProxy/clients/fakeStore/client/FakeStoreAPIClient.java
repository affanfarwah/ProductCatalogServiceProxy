package com.example.ProductCatalogServiceProxy.clients.fakeStore.client;

import com.example.ProductCatalogServiceProxy.clients.fakeStore.dto.FakeStoreProductDTO;
import jakarta.annotation.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * // This class is a CLIENT for FakeStore API
 * // ----------------------------------------
 * // Instead of writing RestTemplate code again and again
 * // inside Service classes, we move all external API calling
 * // logic into a separate class.
 * //
 * // WHY?
 * // ----
 * // Separation of Responsibility:
 * //
 * // Controller  -> handles HTTP requests from frontend/client
 * // Service     -> contains business logic
 * // Client      -> talks to external APIs (FakeStore API)
 * //
 * // Without this class:
 * // Service class becomes messy because it contains:
 * // 1. Business logic
 * // 2. External API calling logic
 * //
 * // With this class:
 * // Service only says:
 * // "Give me product"
 * // Client handles HOW to call FakeStore API
 * //
 * // BENEFITS
 * // --------
 * // 1. Cleaner code
 * // 2. Reusable API calling methods
 * // 3. Easy to maintain
 * // 4. If FakeStore API URL changes,
 * //    only this class changes
 * // 5. Easy to replace FakeStore with another API later
 * //
 * // FLOW
 * // ----
 * // Browser/Postman
 * //        ↓
 * // Controller
 * //        ↓
 * // Service
 * //        ↓
 * // FakeStoreAPIClient
 * //        ↓
 * // FakeStore API
 * //
 * // Then response comes back in reverse order.
 * //
 * // Example:
 * // Service calls:
 * // fakeStoreAPIClient.getProduct(1L);
 * //
 * // Client internally uses RestTemplate
 * // to call:
 * // https://fakestoreapi.com/products/1
 * //
 * // Client returns FakeStoreProductDTO back to Service.
 */
@Component
public class FakeStoreAPIClient {
    RestTemplateBuilder restTemplateBuilder;
    public FakeStoreAPIClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }
    public FakeStoreProductDTO getProduct(Long productId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDTO fakeStoreProductDTO = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreProductDTO.class, productId).getBody();
        return fakeStoreProductDTO;
    }
    public FakeStoreProductDTO[] getProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDTO[] fakeStoreProductDTOS = restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreProductDTO[].class).getBody();
        return fakeStoreProductDTOS;
    }
    public FakeStoreProductDTO createProduct(FakeStoreProductDTO fakeStoreProductDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.postForEntity(
                "https://fakestoreapi.com/products",
                fakeStoreProductDTO,
                FakeStoreProductDTO.class);
        return fakeStoreProductDTOResponseEntity.getBody();
    }
    public FakeStoreProductDTO updateProduct(Long productId, FakeStoreProductDTO fakeStoreProductDTO) {
        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = requestForEntity(
                HttpMethod.PATCH,
                "https://fakestoreapi.com/products/{id}",
                fakeStoreProductDTO,
                FakeStoreProductDTO.class,
                productId);
        return fakeStoreProductDTOResponseEntity.getBody();
    }
    //helper method
    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url,
                                                   @Nullable Object request, Class<T> responseType,
                                                   Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor =
                restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(
                url,
                httpMethod,
                requestCallback,
                responseExtractor,
                uriVariables
        );
    }

}
