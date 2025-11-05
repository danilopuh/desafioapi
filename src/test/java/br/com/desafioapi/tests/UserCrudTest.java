package br.com.desafioapi.tests;

import br.com.desafioapi.config.TestConfig;
import br.com.desafioapi.models.User;
import br.com.desafioapi.models.UserListResponse;
import br.com.desafioapi.models.ApiResponse;
import br.com.desafioapi.utils.AllureUtils;
import br.com.desafioapi.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Gerenciamento de Usuários")
@Feature("Operações CRUD de Usuários")
public class UserCrudTest extends BaseTest {
    
    private String createdUserId;
    
    @Test(groups = {"smoke", "regression"}, priority = 1, description = "CT001 - Listagem de todos os usuários cadastrados")
    @Story(" Listar Usuários")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica se a API consegue retornar a lista completa de usuários cadastrados no sistema com informações de quantidade e dados dos usuários")
    public void testListAllUsers() {
        logTestStep("Executando GET /usuarios para listar todos os usuários");
        
        Response response = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        AllureUtils.attachResponseBody(response);
        
        // Usar JsonPath para obter dados da resposta
        Integer quantidade = response.jsonPath().getInt("quantidade");
        Object usuarios = response.jsonPath().get("usuarios");
        
        Assert.assertNotNull(quantidade, "Campo quantidade deve estar presente");
        Assert.assertTrue(quantidade >= 0, "Quantidade de usuários deve ser maior ou igual a 0");
        Assert.assertNotNull(usuarios, "Lista de usuários não deve ser nula");
        
        logger.info("Teste de listagem de usuários executado com sucesso. Total: {}", quantidade);
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 2, description = "CT002 - Criação de novo usuário com dados válidos")
    @Story("Criar Usuário")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica se é possível criar um novo usuário fornecendo dados válidos (nome, email, password, administrador) e se a API retorna o ID do usuário criado")
    public void testCreateUser() {
        logTestStep("Executando POST /usuarios para criar um novo usuário");
        
        User newUser = TestDataGenerator.generateValidUser();
        AllureUtils.attachRequestBody(newUser.toString());
        
        Response response = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .body(newUser)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract()
                .response();
        
        AllureUtils.attachResponseBody(response);
        
        // Usar JsonPath para obter dados da resposta
        String message = response.jsonPath().getString("message");
        createdUserId = response.jsonPath().getString("_id");
        
        Assert.assertEquals(message, "Cadastro realizado com sucesso", "Mensagem de sucesso deve estar presente");
        
        Assert.assertNotNull(createdUserId, "ID do usuário criado não deve ser nulo");
        Assert.assertFalse(createdUserId.isEmpty(), "ID do usuário criado não deve estar vazio");
        
        logger.info("Usuário criado com sucesso. ID: {}", createdUserId);
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 3, dependsOnMethods = "testCreateUser", description = "CT003 - Busca de usuário específico por ID")
    @Story(" Buscar Usuário por ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica se é possível localizar e retornar os dados completos de um usuário específico utilizando seu ID único como parâmetro de busca")
    public void testGetUserById() {
        logTestStep("Executando GET /usuarios/{id} para buscar usuário por ID");
        
        Assert.assertNotNull(createdUserId, "ID do usuário deve estar disponível");
        
        Response response = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .pathParam("id", createdUserId)
                .when()
                .get("/usuarios/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        AllureUtils.attachResponseBody(response);
        
        // Usar JsonPath para obter dados da resposta
        String foundId = response.jsonPath().getString("_id");
        String nome = response.jsonPath().getString("nome");
        String email = response.jsonPath().getString("email");
        String administrador = response.jsonPath().getString("administrador");
        
        Assert.assertEquals(foundId, createdUserId, 
            "ID do usuário encontrado deve corresponder ao ID pesquisado");
        Assert.assertNotNull(nome, "Nome não deve ser nulo");
        Assert.assertNotNull(email, "Email não deve ser nulo");
        Assert.assertNotNull(administrador, "Campo administrador não deve ser nulo");
        
        logger.info("Usuário encontrado com sucesso - ID: {}, Nome: {}", foundId, nome);
    }
    
    @Test(groups = {"regression"}, priority = 4, dependsOnMethods = "testCreateUser", description = "CT004 - Atualização de dados de usuário existente")
    @Story(" Atualizar Usuário")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica se é possível modificar os dados de um usuário já cadastrado, alterando nome, email, senha e status de administrador através de requisição PUT")
    public void testUpdateUser() {
        logTestStep("Executando PUT /usuarios/{id} para atualizar usuário");
        
        Assert.assertNotNull(createdUserId, "ID do usuário deve estar disponível");
        
        User updatedUser = TestDataGenerator.generateValidUser();
        AllureUtils.attachRequestBody(updatedUser.toString());
        
        Response response = given()
                .spec(TestConfig.getAuthenticatedRequestSpec(adminToken))
                .pathParam("id", createdUserId)
                .body(updatedUser)
                .when()
                .put("/usuarios/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Usar JsonPath para validar resposta
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Registro alterado com sucesso", "Mensagem de sucesso deve estar presente");
        
        AllureUtils.attachResponseBody(response);
        
        // Verificar se a atualização foi realizada
        Response verificationResponse = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .pathParam("id", createdUserId)
                .when()
                .get("/usuarios/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Usar JsonPath para verificar atualização
        String verifiedNome = verificationResponse.jsonPath().getString("nome");
        String verifiedEmail = verificationResponse.jsonPath().getString("email");
        
        Assert.assertEquals(verifiedNome, updatedUser.getNome(),
            "Nome do usuário deve ter sido atualizado");
        Assert.assertEquals(verifiedEmail, updatedUser.getEmail(),
            "Email do usuário deve ter sido atualizado");
        
        logger.info("Usuário atualizado com sucesso - Nome: {}, Email: {}", verifiedNome, verifiedEmail);
    }
    
    @Test(groups = {"regression"}, priority = 5, dependsOnMethods = {"testCreateUser", "testGetUserById", "testUpdateUser"}, description = "CT005 - Exclusão de usuário existente")
        @Story("Excluir Usuário")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica se é possível remover permanentemente um usuário do sistema utilizando token de administrador e confirma que o usuário não pode mais ser encontrado após a exclusão")
    public void testDeleteUser() {
        logTestStep("Executando DELETE /usuarios/{id} para excluir usuário");
        
        Assert.assertNotNull(createdUserId, "ID do usuário deve estar disponível");
        
        Response response = given()
                .spec(TestConfig.getAuthenticatedRequestSpec(adminToken))
                .pathParam("id", createdUserId)
                .when()
                .delete("/usuarios/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Usar JsonPath para validar resposta
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Registro excluído com sucesso", "Mensagem de sucesso deve estar presente");
        
        AllureUtils.attachResponseBody(response);
        
        // Verificar se o usuário foi realmente excluído
        given()
                .spec(TestConfig.getDefaultRequestSpec())
                .pathParam("id", createdUserId)
                .when()
                .get("/usuarios/{id}")
                .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
        
        logger.info("Usuário excluído com sucesso. ID: {}", createdUserId);
    }
    
    @Test(groups = {"smoke", "regression"}, priority = 6, description = "CT006 - Criação de usuário com privilégios administrativos")
    @Story("Criar Usuário Administrador")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica se é possível criar um usuário com flag de administrador ativa, garantindo que tenha as permissões necessárias para gerenciar outros usuários no sistema")
    public void testCreateAdminUser() {
        logTestStep("Executando POST /usuarios para criar usuário administrador");
        
        User adminUser = TestDataGenerator.generateAdminUser();
        AllureUtils.attachRequestBody(adminUser.toString());
        
        Response response = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .body(adminUser)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract()
                .response();
        
        AllureUtils.attachResponseBody(response);
        
        // Usar JsonPath para obter dados da resposta
        String message = response.jsonPath().getString("message");
        String adminUserId = response.jsonPath().getString("_id");
        
        Assert.assertEquals(message, "Cadastro realizado com sucesso", "Mensagem de sucesso deve estar presente");
        Assert.assertNotNull(adminUserId, "ID do usuário admin não deve ser nulo");
        
        // Verificar se o usuário foi criado como administrador
        Response verificationResponse = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .pathParam("id", adminUserId)
                .when()
                .get("/usuarios/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Usar JsonPath para verificar se é admin
        String administrador = verificationResponse.jsonPath().getString("administrador");
        String adminNome = verificationResponse.jsonPath().getString("nome");
        
        Assert.assertEquals(administrador, "true", 
            "Usuário deve ter sido criado com privilégios de administrador");
        
        logger.info("Usuário administrador criado com sucesso - Nome: {}, Admin: {}", adminNome, administrador);
        
        // Limpeza - excluir usuário criado
        given()
                .spec(TestConfig.getAuthenticatedRequestSpec(adminToken))
                .pathParam("id", adminUserId)
                .when()
                .delete("/usuarios/{id}");
    }
}