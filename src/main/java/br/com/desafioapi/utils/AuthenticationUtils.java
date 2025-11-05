package br.com.desafioapi.utils;

import br.com.desafioapi.config.TestConfig;
import br.com.desafioapi.models.LoginRequest;
import br.com.desafioapi.models.LoginResponse;
import br.com.desafioapi.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class AuthenticationUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUtils.class);
    private static String cachedAdminToken;
    private static final Object tokenLock = new Object();
    
    @Step("Realizar login e obter token JWT")
    public static String getAuthToken(String email, String password) {
        logger.info("Obtendo token de autenticação para o usuário: {}", email);
        
        LoginRequest loginRequest = new LoginRequest(email, password);
        
        Response response = given()
                .spec(TestConfig.getDefaultRequestSpec())
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        LoginResponse loginResponse = response.as(LoginResponse.class);
        String token = loginResponse.getAuthorization();
        
        logger.info("Token obtido com sucesso para o usuário: {}", email);
        return token;
    }
    
    @Step("Obter token de administrador")
    public static String getAdminToken() {
        synchronized (tokenLock) {
            if (cachedAdminToken == null || !isTokenValid(cachedAdminToken)) {
                logger.info("Obtendo novo token de administrador");
                cachedAdminToken = getAuthToken(
                        TestConfig.getConfig().adminEmail(),
                        TestConfig.getConfig().adminPassword()
                );
            }
            return cachedAdminToken;
        }
    }
    
    @Step("Verificar se token é válido")
    public static boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        try {
            Response response = given()
                    .spec(TestConfig.getAuthenticatedRequestSpec(token))
                    .when()
                    .get("/usuarios")
                    .then()
                    .extract()
                    .response();
            
            return response.statusCode() == 200;
        } catch (Exception e) {
            logger.warn("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }
    
    @Step("Criar usuário administrador para testes")
    public static String createAdminUser() {
        User adminUser = User.builder()
                .nome(TestConfig.getConfig().adminName())
                .email(TestConfig.getConfig().adminEmail())
                .password(TestConfig.getConfig().adminPassword())
                .administrador("true")
                .build();
        
        try {
            Response response = given()
                    .spec(TestConfig.getDefaultRequestSpec())
                    .body(adminUser)
                    .when()
                    .post("/usuarios")
                    .then()
                    .extract()
                    .response();
            
            if (response.statusCode() == 201) {
                logger.info("Usuário administrador criado com sucesso");
                return getAuthToken(adminUser.getEmail(), adminUser.getPassword());
            } else if (response.statusCode() == 400 && 
                       response.getBody().asString().contains("Este email já está sendo usado")) {
                logger.info("Usuário administrador já existe, fazendo login");
                return getAuthToken(adminUser.getEmail(), adminUser.getPassword());
            } else {
                throw new RuntimeException("Falha ao criar usuário administrador: " + response.getBody().asString());
            }
        } catch (Exception e) {
            logger.error("Erro ao criar/logar usuário administrador", e);
            throw e;
        }
    }
    
    @Step("Limpar token cacheado")
    public static void clearCachedToken() {
        synchronized (tokenLock) {
            cachedAdminToken = null;
            logger.info("Token cacheado limpo");
        }
    }
}