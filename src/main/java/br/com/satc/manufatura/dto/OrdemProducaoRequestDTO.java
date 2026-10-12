package br.com.satc.manufatura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OrdemProducaoRequestDTO(
        @NotNull(message = "é obrigatório") Long maquinaId,
        @NotNull(message = "é obrigatório") Long operadorId,
        @NotBlank(message = "é obrigatório") @Size(max = 100, message = "deve ter no máximo 100 caracteres") String produto,
        @NotNull(message = "é obrigatório") @Positive(message = "deve ser maior que zero") Integer quantidadePlanejada) {
}
