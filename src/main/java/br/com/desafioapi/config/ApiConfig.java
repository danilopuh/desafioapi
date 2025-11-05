package br.com.desafioapi.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ApiConfig extends Config {
    
    @Key("api.base.url")
    @DefaultValue("https://serverest.dev")
    String baseUrl();
    
    @Key("api.timeout")
    @DefaultValue("30000")
    int timeout();
    
    @Key("api.rate.limit.requests")
    @DefaultValue("100")
    int rateLimitRequests();
    
    @Key("api.rate.limit.window")
    @DefaultValue("60000")
    int rateLimitWindow();
    
    @Key("test.admin.email")
    @DefaultValue("admin@teste.com")
    String adminEmail();
    
    @Key("test.admin.password")
    @DefaultValue("teste123")
    String adminPassword();
    
    @Key("test.admin.name")
    @DefaultValue("Administrador Teste")
    String adminName();
    
    @Key("test.environment")
    @DefaultValue("dev")
    String environment();
    
    @Key("allure.results.directory")
    @DefaultValue("target/allure-results")
    String allureResultsDirectory();
}