package br.com.satc.manufatura.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FinalizarOrdemRequestDTO(
        @NotNull(message = "é obrigatório") @Min(value = 0, message = "não pode ser negativa") Integer quantidadeProduzida,
        @NotNull(message = "é obrigatório") @Min(value = 0, message = "não pode ser negativa") Integer quantidadeDefeito) {
}
