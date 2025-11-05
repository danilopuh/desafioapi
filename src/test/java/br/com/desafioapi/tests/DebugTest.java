package br.com.desafioapi.tests;

import br.com.desafioapi.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DebugTest {
    
    @Test
    public void testCreateUserDebug() {
        // Configurar RestAssured
        io.restassured.RestAssured.baseURI = "https://serverest.dev";
        
        // Dados simples para teste
        String userJson = "{\n" +
                "  \"nome\": \"Usuário Teste Debug\",\n" +
                "  \"email\": \"debug" + System.currentTimeMillis() + "@teste.com\",\n" +
                "  \"password\": \"teste123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";
        
        System.out.println("JSON sendo enviado:");
        System.out.println(userJson);
        
        Response response = given()
                .contentType("application/json")
                .body(userJson)
                .when()
                .post("/usuarios")
                .then()
                .extract()
                .response();
        
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body:");
        System.out.println(response.getBody().asString());
    }
    
    @Test
    public void testCreateUserWithObject() {
        // Configurar RestAssured
        io.restassured.RestAssured.baseURI = "https://serverest.dev";
        
        // Testar com objeto User
        User newUser = User.builder()
                .nome("Usuário Teste Objeto " + System.currentTimeMillis())
                .email("objeto_" + System.currentTimeMillis() + "@qa.com")
                .password("teste123")
                .administrador("true")
                .build();
        
        // Ver como o Jackson serializa
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(newUser);
            System.out.println("JSON serializado pelo Jackson:");
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Response response = given()
                .contentType("application/json")
                .body(newUser)
                .when()
                .post("/usuarios")
                .then()
                .extract()
                .response();
        
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body:");
        System.out.println(response.getBody().asString());
    }
}