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

@Epic("Gerenciamento de Usuários - Corrigido")
@Feature("Operações CRUD de Usuários - Versão Funcional")
public class UserCrudFixedTest extends BaseTest {
    
    private String createdUserId;
    
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Story("Listar Usuários - Corrigido")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar se é possível listar todos os usuários cadastrados")
    public void testListAllUsersFixed() {
        logTestStep("Executando GET /usuarios para listar todos os usuários");
        
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
        
        // Validações básicas sem problemas de parsing
        Assert.assertTrue(response.getStatusCode() == 200, "Status code deve ser 200");
        Assert.assertTrue(response.getBody().asString().contains("usuarios"), "Response deve conter 'usuarios'");
        Assert.assertTrue(response.getBody().asString().contains("quantidade"), "Response deve conter 'quantidade'");
        
        logTestStep("Teste de listagem concluído com sucesso");
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 2)
    @Story("Criar Usuário - Corrigido")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar se é possível criar um novo usuário")
    public void testCreateUserFixed() {
        logTestStep("Criando usuário para teste CRUD");
        
        User newUser = User.builder()
                .nome("Usuário Teste CRUD " + System.currentTimeMillis())
                .email("crud_" + System.currentTimeMillis() + "@qa.com")
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
                .extract()
                .response();
        
        // Extrair ID de forma simples
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Cadastro realizado com sucesso"), "Mensagem de sucesso deve estar presente");
        Assert.assertTrue(responseBody.contains("_id"), "ID deve estar presente na resposta");
        
        // Extrair ID usando JsonPath do RestAssured
        try {
            createdUserId = response.jsonPath().getString("_id");
            Assert.assertNotNull(createdUserId, "ID do usuário criado não deve ser null");
            logTestStep("Usuário criado com sucesso. ID: " + createdUserId);
        } catch (Exception e) {
            logTestStep("Erro ao extrair ID: " + e.getMessage());
            logTestStep("Response body: " + responseBody);
            Assert.fail("Não foi possível extrair o ID do usuário criado: " + e.getMessage());
        }
    }
    
    @Test(groups = {"regression"}, priority = 3, dependsOnMethods = {"testCreateUserFixed"})
    @Story("Buscar Usuário por ID - Corrigido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar se é possível buscar um usuário específico pelo ID")
    public void testGetUserByIdFixed() {
        if (createdUserId == null) {
            Assert.fail("Usuário precisa ser criado primeiro");
        }
        
        logTestStep("Buscando usuário por ID: " + createdUserId);
        
        Response response = given()
                .contentType("application/json")
                .when()
                .get("/usuarios/" + createdUserId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("nome"), "Response deve conter 'nome'");
        Assert.assertTrue(responseBody.contains("email"), "Response deve conter 'email'");
        Assert.assertTrue(responseBody.contains(createdUserId), "Response deve conter o ID correto");
        
        logTestStep("Usuário encontrado com sucesso");
    }
    
    @Test(groups = {"regression"}, priority = 4, dependsOnMethods = {"testGetUserByIdFixed"})
    @Story("Atualizar Usuário - Corrigido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar se é possível atualizar os dados de um usuário")
    public void testUpdateUserFixed() {
        if (createdUserId == null) {
            Assert.fail("Usuário precisa ser criado primeiro");
        }
        
        logTestStep("Atualizando usuário: " + createdUserId);
        
        User updateUser = User.builder()
                .nome("Usuário Atualizado " + System.currentTimeMillis())
                .email("atualizado_" + System.currentTimeMillis() + "@qa.com")
                .password("nova123")
                .administrador("false")
                .build();
        
        Response response = given()
                .contentType("application/json")
                .body(updateUser)
                .when()
                .put("/usuarios/" + createdUserId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Registro alterado com sucesso"), "Mensagem de sucesso deve estar presente");
        
        logTestStep("Usuário atualizado com sucesso");
    }
    
    @Test(groups = {"regression"}, priority = 5, dependsOnMethods = {"testUpdateUserFixed"})
    @Story("Deletar Usuário - Corrigido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar se é possível deletar um usuário")
    public void testDeleteUserFixed() {
        if (createdUserId == null) {
            Assert.fail("Usuário precisa ser criado primeiro");
        }
        
        logTestStep("Deletando usuário: " + createdUserId);
        
        Response response = given()
                .contentType("application/json")
                .when()
                .delete("/usuarios/" + createdUserId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Registro excluído com sucesso"), "Mensagem de sucesso deve estar presente");
        
        logTestStep("Usuário deletado com sucesso");
        
        // Verificar se usuário foi realmente deletado
        given()
                .contentType("application/json")
                .when()
                .get("/usuarios/" + createdUserId)
                .then()
                .statusCode(400); // Usuário não encontrado
        
        logTestStep("Confirmado: usuário não existe mais no sistema");
    }
}