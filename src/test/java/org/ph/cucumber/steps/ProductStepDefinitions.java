package org.ph.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.ph.domain.model.Product;
import org.ph.infrastructure.adapter.rest.dto.ProductRequest;
import org.ph.infrastructure.adapter.rest.dto.ProductResponse;
import org.ph.infrastructure.adapter.rest.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Step definitions for the product feature.
 * This class implements the Given-When-Then steps from the feature file.
 */
@org.springframework.stereotype.Component
public class ProductStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductMapper productMapper;

    private ProductRequest productRequest;
    private ResponseEntity<ProductResponse> productResponse;
    private ResponseEntity<ProductResponse[]> productsResponse;
    private UUID productId;

    @Given("I have a product with the following details:")
    public void iHaveAProductWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);

        productRequest = ProductRequest.builder()
                .name(row.get("name"))
                .description(row.get("description"))
                .price(new BigDecimal(row.get("price")))
                .stockQuantity(Integer.parseInt(row.get("stockQuantity")))
                .build();
    }

    @When("I create the product")
    public void iCreateTheProduct() {
        productResponse = restTemplate.postForEntity("/api/products", productRequest, ProductResponse.class);
    }

    @Then("the product should be created successfully")
    public void theProductShouldBeCreatedSuccessfully() {
        Assertions.assertEquals(HttpStatus.CREATED, productResponse.getStatusCode());
    }

    @Then("the product should have an ID")
    public void theProductShouldHaveAnID() {
        Assertions.assertNotNull(productResponse.getBody());
        Assertions.assertNotNull(productResponse.getBody().getId());
        productId = productResponse.getBody().getId();
    }

    @Then("the product details should match the input")
    public void theProductDetailsShouldMatchTheInput() {
        Assertions.assertNotNull(productResponse.getBody());
        Assertions.assertEquals(productRequest.getName(), productResponse.getBody().getName());
        Assertions.assertEquals(productRequest.getDescription(), productResponse.getBody().getDescription());
        Assertions.assertEquals(productRequest.getPrice(), productResponse.getBody().getPrice());
        Assertions.assertEquals(productRequest.getStockQuantity(), productResponse.getBody().getStockQuantity());
    }

    @Given("I have a product in the system")
    public void iHaveAProductInTheSystem() {
        // Create a product first
        productRequest = ProductRequest.builder()
                .name("Test Product")
                .description("A test product")
                .price(new BigDecimal("10.99"))
                .stockQuantity(100)
                .build();

        productResponse = restTemplate.postForEntity("/api/products", productRequest, ProductResponse.class);
        productId = productResponse.getBody().getId();
    }

    @When("I request the product by its ID")
    public void iRequestTheProductByItsID() {
        productResponse = restTemplate.getForEntity("/api/products/" + productId, ProductResponse.class);
    }

    @Then("I should receive the product details")
    public void iShouldReceiveTheProductDetails() {
        Assertions.assertEquals(HttpStatus.OK, productResponse.getStatusCode());
        Assertions.assertNotNull(productResponse.getBody());
        Assertions.assertEquals(productId, productResponse.getBody().getId());
    }

    @Given("I have multiple products in the system")
    public void iHaveMultipleProductsInTheSystem() {
        // Create first product
        ProductRequest product1 = ProductRequest.builder()
                .name("Product 1")
                .description("First test product")
                .price(new BigDecimal("10.99"))
                .stockQuantity(100)
                .build();
        restTemplate.postForEntity("/api/products", product1, ProductResponse.class);

        // Create second product
        ProductRequest product2 = ProductRequest.builder()
                .name("Product 2")
                .description("Second test product")
                .price(new BigDecimal("20.99"))
                .stockQuantity(200)
                .build();
        restTemplate.postForEntity("/api/products", product2, ProductResponse.class);
    }

    @When("I request all products")
    public void iRequestAllProducts() {
        productsResponse = restTemplate.getForEntity("/api/products", ProductResponse[].class);
    }

    @Then("I should receive a list of all products")
    public void iShouldReceiveAListOfAllProducts() {
        Assertions.assertEquals(HttpStatus.OK, productsResponse.getStatusCode());
        Assertions.assertNotNull(productsResponse.getBody());
        Assertions.assertTrue(productsResponse.getBody().length >= 2);
    }

    @When("I update the product with the following details:")
    public void iUpdateTheProductWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);

        productRequest = ProductRequest.builder()
                .name(row.get("name"))
                .description(row.get("description"))
                .price(new BigDecimal(row.get("price")))
                .stockQuantity(Integer.parseInt(row.get("stockQuantity")))
                .build();

        HttpEntity<ProductRequest> requestEntity = new HttpEntity<>(productRequest);
        productResponse = restTemplate.exchange(
                "/api/products/" + productId,
                HttpMethod.PUT,
                requestEntity,
                ProductResponse.class);
    }

    @Then("the product should be updated successfully")
    public void theProductShouldBeUpdatedSuccessfully() {
        Assertions.assertEquals(HttpStatus.OK, productResponse.getStatusCode());
    }

    @Then("the product details should match the updated values")
    public void theProductDetailsShouldMatchTheUpdatedValues() {
        Assertions.assertNotNull(productResponse.getBody());
        Assertions.assertEquals(productRequest.getName(), productResponse.getBody().getName());
        Assertions.assertEquals(productRequest.getDescription(), productResponse.getBody().getDescription());
        Assertions.assertEquals(productRequest.getPrice(), productResponse.getBody().getPrice());
        Assertions.assertEquals(productRequest.getStockQuantity(), productResponse.getBody().getStockQuantity());
    }

    @When("I delete the product")
    public void iDeleteTheProduct() {
        restTemplate.delete("/api/products/" + productId);
    }

    @Then("the product should be deleted successfully")
    public void theProductShouldBeDeletedSuccessfully() {
        // This step is verified in the next step
    }

    @Then("the product should no longer be available")
    public void theProductShouldNoLongerBeAvailable() {
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
                "/api/products/" + productId,
                ProductResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
