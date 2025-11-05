package br.com.desafioapi.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterClass;

public class BaseTest {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected String adminToken;
    
    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        try {
            logger.info("=== Iniciando configuração da classe de teste ===");
            
            // Configurar RestAssured básico
            io.restassured.RestAssured.baseURI = "https://serverest.dev";
            
            // Token de admin padrão - corrigido para não ter Bearer duplicado
            adminToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNTcxOTQwOTA3fQ.hdXZjdFUXhvbfL1xyJlJhVJjqEVU7f7RZSsA1lFsHQM";
            
            logger.info("=== Configuração da classe concluída com sucesso ===");
        } catch (Exception e) {
            logger.error("Erro durante configuração da classe: {}", e.getMessage());
            throw e;
        }
    }
    
    @BeforeMethod(alwaysRun = true)
    public void setupMethod() {
        try {
            logger.info("--- Iniciando método de teste ---");
        } catch (Exception e) {
            logger.error("Erro durante configuração do método: {}", e.getMessage());
            throw e;
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void teardownMethod() {
        logger.info("--- Finalizando método de teste ---");
    }
    
    @AfterClass(alwaysRun = true)
    public void teardownClass() {
        logger.info("=== Finalizando classe de teste ===");
    }
    
    protected void logTestStep(String step) {
        logger.info("PASSO: {}", step);
    }
}