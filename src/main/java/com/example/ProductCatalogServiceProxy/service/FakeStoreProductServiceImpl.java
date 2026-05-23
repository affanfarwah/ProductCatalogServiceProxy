package com.example.ProductCatalogServiceProxy.service;

import com.example.ProductCatalogServiceProxy.clients.fakeStore.client.FakeStoreAPIClient;
import com.example.ProductCatalogServiceProxy.clients.fakeStore.dto.FakeStoreProductDTO;
import com.example.ProductCatalogServiceProxy.model.Category;
import com.example.ProductCatalogServiceProxy.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//@Service //one bean at a time, means one service class at a time
public class FakeStoreProductServiceImpl implements ProductService {
    RestTemplateBuilder restTemplateBuilder; // no longer needed, because all API calls moved to Client.
    FakeStoreAPIClient fakeStoreAPIClient;

    FakeStoreProductServiceImpl(RestTemplateBuilder restTemplateBuilder, FakeStoreAPIClient fakeStoreAPIClient) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreAPIClient = fakeStoreAPIClient;
    }
    // issue using List<> while calling restTemplate.getForEntity
//    @Override
//    public List<Product> getProducts() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        // TRYING to get list of FakeStoreProductDTO objects
//        // from FakeStore API response // But this line is WRONG and does NOT compile: // List<FakeStoreProductDTO>.class
//        // Reason: // Java Generics use Type Erasure
//        // At runtime: // List<FakeStoreProductDTO>
//        //            // becomes simply:// List
//        // So JVM does NOT know what FakeStoreProductDTO is here,
//        // There is NO runtime class called: // List<FakeStoreProductDTO>.class
//        // That is why Spring cannot deserialize JSON into it
//        // Correct approach: // Use FakeStoreProductDTO[].class instead
//        // Arrays preserve runtime type information
//        // Correct: FakeStoreProductDTO[].class
//        // Wrong: // List<FakeStoreProductDTO>.class
//
//        List<FakeStoreProductDTO> FakeStoreProductDTOs = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", List<FakeStoreProductDTO>.class).getBody();
//        return new ArrayList<>();
//    }
    // issue is solved by passing as array[]
//    @Override
//    public List<Product> getProducts() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        FakeStoreProductDTO[] fakeStoreProductDTOS = restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreProductDTO[].class).getBody();
//        List<Product> products = new ArrayList<>();
//        for(FakeStoreProductDTO fakeStoreProductDTO : fakeStoreProductDTOS) {
//            products.add(getProduct(fakeStoreProductDTO));
//        }
//        return products;
//    }

    //calling via FakeStoreAPIClient
    @Override
    public List<Product> getProducts() {
        FakeStoreProductDTO[] fakeStoreProductDTOS = fakeStoreAPIClient.getProducts();
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDTO fakeStoreProductDTO : fakeStoreProductDTOS) {
            products.add(getProduct(fakeStoreProductDTO));
        }
        return products;
    }

    // get 1 product from api, calling from service logic, this is not good, better to create a separate class
    // where it's only job is to call the 3rd party apis
//    @Override
//    public Product getProduct(Long productId) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        FakeStoreProductDTO fakeStoreProductDTO = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreProductDTO.class, productId).getBody();
//        return getProduct(fakeStoreProductDTO);
//    }

    // passing this work to done by client class, controller calls -> service calls -> fakeStoreApi client calls -> 3rd party api
    // 3rd party should be call via client class(fakeStoreApiClient) not by a service class.
    @Override
    public Product getProduct(Long productId) {
        return getProduct(fakeStoreAPIClient.getProduct(productId));
    }
//    //create product using api, working with FakeStoreProductDTO
//    @Override
//    public Product createProduct(FakeStoreProductDTO fakeStoreProductDTO) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.postForEntity(
//                "https://fakestoreapi.com/products",
//                fakeStoreProductDTO,
//                FakeStoreProductDTO.class);
//        return getProduct(fakeStoreProductDTOResponseEntity.getBody());
//    }
    //create product using api, working with only Product
//    @Override
//    public Product createProduct(Product product) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.postForEntity(
//                "https://fakestoreapi.com/products",
//                product,
//                FakeStoreProductDTO.class);
//        return getProduct(fakeStoreProductDTOResponseEntity.getBody());
//    }

    //let FakeStoreAPIClient class the 3rd party api instead of service class
    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = getFakeStoreProductDTO(product);
        return getProduct(fakeStoreAPIClient.createProduct(fakeStoreProductDTO)); //got response, then converts into Product and returns
    }
    // there is a bug in restTemplate.patchForObject
//    @Override
//    public Product updateProduct(Long id, Product product) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        FakeStoreProductDTO fakeStoreProductDTO = restTemplate.patchForObject("https://fakestoreapi.com/products/{id}",product, FakeStoreProductDTO.class, id);
//        Product resultantProduct = getProduct(fakeStoreProductDTO);
//        return resultantProduct;
//    }

    // so, we create a helper method called as requestForEntity, still this also not works
    // to work, we can do again conversion from product to productDTO, in this we are passing
    // product which is not working due to some bug in this builtin method, so it will work in productDto
    @Override
    public Product updateProduct(Long id, Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = getFakeStoreProductDTO(product);
        FakeStoreProductDTO resultantFakeStoreProductDTO = fakeStoreAPIClient.updateProduct(id, fakeStoreProductDTO);
        return getProduct(resultantFakeStoreProductDTO);
    }

    @Override
    public Product deleteProduct(Long id) {
        return null;
    }

    // bugs in restTemplate.patchForObject ie implementing our own
    private  <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
    //helper method for converting productDTO into Product
    private Product getProduct(FakeStoreProductDTO fakeStoreProductDTO) {
        Product product = new Product();
        product.setTitle(fakeStoreProductDTO.getTitle());
        product.setDescription((fakeStoreProductDTO.getDescription()));
        product.setPrice(fakeStoreProductDTO.getPrice());
        product.setImageUrl(fakeStoreProductDTO.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDTO.getCategory());
        product.setCategory(category);
        product.setId(fakeStoreProductDTO.getId());

        return product;
    }
    private FakeStoreProductDTO getFakeStoreProductDTO(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();
        fakeStoreProductDTO.setId(product.getId());
        fakeStoreProductDTO.setCategory(product.getCategory().getName());
        fakeStoreProductDTO.setImage(product.getImageUrl());
        fakeStoreProductDTO.setTitle(product.getTitle());
        fakeStoreProductDTO.setDescription(product.getDescription());
        fakeStoreProductDTO.setPrice(product.getPrice());

        return fakeStoreProductDTO;
    }
}
