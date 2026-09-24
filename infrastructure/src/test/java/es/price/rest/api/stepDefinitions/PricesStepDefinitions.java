package es.price.rest.api.stepDefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.model.PriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class PricesStepDefinitions {

  private static final String PRICES_ENDPOINT = "/prices";

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private ResponseEntity<String> response;
  private String productId;
  private String brandId;

  @Given("the product {string} and brand {string} exist")
  public void the_product_and_brand_exist(String productId, String brandId) {

    this.productId = productId;
    this.brandId = brandId;
  }

  @When("I request the price at {string}")
  public void i_request_the_price_at(String applicationDate) {

    var url = PRICES_ENDPOINT
            + "?applicationDate=" + applicationDate
            + "&productId=" + productId
            + "&brandId=" + brandId;

    response = restTemplate.getForEntity(url, String.class);
  }

  @Then("the response status should be {int}")
  public void the_response_status_should_be(int expectedStatus) {
    // Assert the HTTP contract before checking the response body.
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(expectedStatus));
  }

  @Then("the returned price list should be {string}")
  public void the_returned_price_list_should_be(String expectedPriceList) throws IOException {
    // Assert the selected tariff, which is the main business outcome.
    assertThat(priceResponse().getPriceList()).isEqualTo(expectedPriceList);
  }

  @Then("the returned price should be {double}")
  public void the_returned_price_should_be(double expectedPrice) throws IOException {
    // Assert the final amount returned by the REST API.
    assertThat(priceResponse().getPrice()).isEqualTo(expectedPrice);
  }

  @Then("the returned product should be {string}")
  public void the_returned_product_should_be(String expectedProductId) throws IOException {
    // Assert that the response belongs to the requested product.
    assertThat(priceResponse().getProductId()).isEqualTo(expectedProductId);
  }

  @Then("the returned brand should be {string}")
  public void the_returned_brand_should_be(String expectedBrandId) throws IOException {
    // Assert that the response belongs to the requested brand.
    assertThat(priceResponse().getBrandId()).isEqualTo(expectedBrandId);
  }

  @Given("the product {string} does not exist")
  public void the_product_does_not_exist(String productId) {
    // Use a product identifier that is not present in data.sql.
    this.productId = productId;
    this.brandId = "1";
  }

  @When("I request the price")
  public void i_request_the_price() {
    // Execute the REST request for the configured product.
    i_request_the_price_at("2020-06-14T10:00:00Z");
  }

  @Then("the response should contain the error message {string}")
  public void the_response_should_contain_the_error_message(String expectedMessage)
          throws IOException {
    // Assert the error contract without depending on a success DTO.
    JsonNode body = objectMapper.readTree(
            Objects.requireNonNull(response.getBody(), "The error response body must not be null"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(body.path("message").asText()).isEqualTo(expectedMessage);
    assertThat(body.path("code").asInt()).isEqualTo(404);
  }

  private PriceResponse priceResponse() throws IOException {
    return objectMapper.readValue(
            Objects.requireNonNull(response.getBody(), "The response body must not be null"),
            PriceResponse.class);
  }

}
