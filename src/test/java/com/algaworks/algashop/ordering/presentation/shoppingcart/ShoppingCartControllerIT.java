package com.algaworks.algashop.ordering.presentation.shoppingcart;

import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashSet;
import java.util.UUID;

import static com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.config.JsonConfig.jsonConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShoppingCartControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    private static final UUID validCustomerId = UUID.fromString("6a6ca34c-e65c-496e-adaf-f872e1784003");

    private WireMockServer wireMockProductCatalog;

    private WireMockServer wireMockRapidex;


    @BeforeEach
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

        initDatabase();

        wireMockRapidex = new WireMockServer(options()
                .port(8780)
                .usingFilesUnderDirectory("src/test/resources/wiremock/rapidex")
                .extensions(new ResponseTemplateTransformer(true)));

        wireMockProductCatalog = new WireMockServer(options()
                .port(8781)
                .usingFilesUnderDirectory("src/test/resources/wiremock/product-catalog")
                .extensions(new ResponseTemplateTransformer(true)));

        wireMockRapidex.start();
        wireMockProductCatalog.start();

        System.out.println("Product Catalog ativo: " + wireMockProductCatalog.isRunning());
        System.out.println("Porta: " + wireMockProductCatalog.port());
        System.out.println("URL: " + wireMockProductCatalog.baseUrl());


    }

    @AfterEach
    public void after() {
        wireMockRapidex.stop();
        wireMockProductCatalog.stop();
    }

    private void initDatabase() {

        customerRepository.saveAndFlush(
                CustomerPersistenceEntityTestDataBuilder.aCustomer().id(validCustomerId).build()
        );

    }


    @Test
    public void shouldCreateShoppingCart() {
        String json = AlgaShopResourceUtils.readContent("json/create-shopping-cart.json");

        ShoppingCartOutput createdShoppingCart = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when()
                .post("/api/v1/shopping-carts")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.not(Matchers.emptyString()))
                .extract()
                .as(ShoppingCartOutput.class);


        Assertions.assertThat(createdShoppingCart.getId()).isNotNull();

        Assertions.assertThat(
                shoppingCartRepository.existsById(createdShoppingCart.getId())
        ).isTrue();
    }

    @Test
    public void shouldNotCreateInvalidShoppingCart() {
        String json = AlgaShopResourceUtils.readContent("json/create-invalid-shopping-cart.json");

         RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when()
                .post("/api/v1/shopping-carts")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Assertions.assertThat(
                shoppingCartRepository.existsByCustomer_Id(validCustomerId)
        ).isFalse();



    }

    @Test
    public void shouldAddProductToShoppingCart() {

        System.out.println(
                "Requisições recebidas: " +
                        wireMockProductCatalog.getAllServeEvents().size()
        );


        var shoppingCartPersistence = existingShoppingCart().items(new HashSet<>())
                .customer(customerRepository.getReferenceById(validCustomerId))
                .build();

        shoppingCartRepository.save(shoppingCartPersistence);

        UUID shoppingCartId = shoppingCartPersistence.getId();

        String json = AlgaShopResourceUtils.readContent("json/add-product-to-shopping-cart.json");

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when()
                .post("/api/v1/shopping-carts/{shoppingCartId}/items", shoppingCartId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var shoppingCartPersistenceEntity = shoppingCartRepository.findById(shoppingCartPersistence.getId()).orElseThrow();
        Assertions.assertThat(shoppingCartPersistenceEntity.getTotalItems()).isEqualTo(2);
    }



}
