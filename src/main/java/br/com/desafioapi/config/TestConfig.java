package br.com.desafioapi.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class TestConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);
    private static final ApiConfig config = ConfigFactory.create(ApiConfig.class);
    
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;
    
    public static void setupRestAssured() {
        logger.info("Configurando RestAssured...");
        
        RestAssured.baseURI = config.baseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Configuração de timeout
        RestAssured.config = RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                        .setParam("http.connection.timeout", config.timeout())
                        .setParam("http.socket.timeout", config.timeout()));
        
        logger.info("RestAssured configurado com baseURI: {}", config.baseUrl());
    }
    
    public static RequestSpecification getDefaultRequestSpec() {
        if (requestSpec == null) {
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(config.baseUrl())
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .log(LogDetail.ALL)
                    .build();
        }
        return requestSpec;
    }
    
    public static ResponseSpecification getDefaultResponseSpec() {
        if (responseSpec == null) {
            responseSpec = new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
        }
        return responseSpec;
    }
    
    public static RequestSpecification getAuthenticatedRequestSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
    
    public static ApiConfig getConfig() {
        return config;
    }
    
    public static void resetSpecs() {
        requestSpec = null;
        responseSpec = null;
        logger.info("Especificações do RestAssured resetadas");
    }
}