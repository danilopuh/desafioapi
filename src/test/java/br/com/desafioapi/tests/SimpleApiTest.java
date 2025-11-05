package br.com.desafioapi.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SimpleApiTest {
    
    @Test(groups = "smoke")
    public void testApiConnection() {
        RestAssured.baseURI = "https://serverest.dev";
        
        Response response = given()
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        System.out.println(" API Response: " + response.getStatusCode());
        System.out.println(" Response Body: " + response.getBody().asString());
        
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("usuarios"));
        
        System.out.println(" Teste executado com sucesso!");
    }
}