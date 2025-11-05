package br.com.desafioapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    
    @JsonProperty("_id")
    private String id; // Para responses de criação
    
    // Método para compatibilidade com código existente
    public String get_id() {
        return this.id;
    }
}