package br.com.desafioapi.tests;


import br.com.desafioapi.models.User;
import br.com.desafioapi.models.UserListResponse;
import br.com.desafioapi.models.ApiResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic(" Testes Básicos de API")
@Feature(" Operações Básicas de Usuários")
public class BasicUserTest {
    
    @Test(groups = {"smoke"}, description = "CT001 - Listagem básica de usuários sem autenticação")
    @Story("Listar Usuários - Teste Básico")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica se a API consegue retornar a lista de usuários cadastrados em teste básico sem configurações complexas, validando funcionalidade fundamental")
    public void testListUsersSimple() {
        System.out.println("Iniciando teste de listagem de usuários...");
        
        // Configurar RestAssured manualmente
        io.restassured.RestAssured.baseURI = "https://serverest.dev";
        
        Response response = given()
                .contentType("application/json")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("usuarios", notNullValue())
                .body("quantidade", greaterThanOrEqualTo(0))
                .extract()
                .response();
        
        // Validações adicionais
        UserListResponse userListResponse = response.as(UserListResponse.class);
        Assert.assertNotNull(userListResponse.getUsers(), "Lista de usuários não deve ser null");
        Assert.assertTrue(userListResponse.getQuantidade() >= 0, "Quantidade deve ser maior ou igual a zero");
        
        System.out.println("Teste concluído com sucesso!");
        System.out.println("Total de usuários encontrados: " + userListResponse.getQuantidade());
    }
    
    @Test(groups = {"smoke"}, description = "CT002 - Criação básica de usuário com dados simples")
    @Story("Criar Usuário - Teste Básico")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica se é possível criar um novo usuário com configuração básica da API, testando funcionalidade essencial de cadastro sem configurações avançadas")
    public void testCreateUserSimple() {
        System.out.println("Iniciando teste de criação de usuário...");
        
        // Configurar RestAssured manualmente
        io.restassured.RestAssured.baseURI = "https://serverest.dev";
        
        // Dados do usuário para criar
        User newUser = User.builder()
                .nome("Usuário Teste API " + System.currentTimeMillis())
                .email("teste_" + System.currentTimeMillis() + "@qa.com")
                .password("teste123")
                .administrador("true")
                .build();
        
        Response response = given()
                .contentType("application/json")
                .body(newUser)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue())
                .extract()
                .response();
        
        // Usar JsonPath em vez de deserialização para evitar conflitos
        String userId = response.jsonPath().getString("_id");
        Assert.assertNotNull(userId, "ID do usuário criado não deve ser null");
        
        System.out.println("Usuário criado com sucesso! ID: " + userId);
    }
}