package br.com.desafioapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    
    @JsonProperty("_id")
    private String id;
    
    private String nome;
    
    private String email;
    
    private String password;
    
    private String administrador;
    
    // Construtor para criação de usuário (sem ID)
    public User(String nome, String email, String password, String administrador) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.administrador = administrador;
    }
    
    // Método para verificar se é administrador - JsonIgnore para não serializar
    @JsonIgnore
    public boolean isAdmin() {
        return "true".equalsIgnoreCase(this.administrador);
    }
    
    // Método para criar cópia sem senha (para logs seguros)
    @JsonIgnore
    public User withoutPassword() {
        return User.builder()
                .id(this.id)
                .nome(this.nome)
                .email(this.email)
                .administrador(this.administrador)
                .build();
    }
}