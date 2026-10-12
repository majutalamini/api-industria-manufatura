package br.com.satc.manufatura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MaquinaRequestDTO(
        @NotBlank(message = "é obrigatório") @Size(max = 20, message = "deve ter no máximo 20 caracteres") String codigo,
        @NotBlank(message = "é obrigatório") @Size(max = 100, message = "deve ter no máximo 100 caracteres") String nome,
        @NotBlank(message = "é obrigatório") @Size(max = 50, message = "deve ter no máximo 50 caracteres") String setor) {
}
