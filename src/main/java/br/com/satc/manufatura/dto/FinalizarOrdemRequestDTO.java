package br.com.satc.manufatura.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Define os dados recebidos ao finalizar uma ordem de produção.
 *
 * As validações impedem que as quantidades sejam omitidas ou informadas com valores negativos antes de os dados
 * chegarem à camada de serviço.
 */
public record FinalizarOrdemRequestDTO(
        // Exige que a quantidade produzida seja informada e não seja negativa.
        @NotNull(message = "é obrigatório") @Min(value = 0, message = "não pode ser negativa") Integer quantidadeProduzida,
        // Exige que a quantidade de itens defeituosos seja informada e não seja negativa.
        @NotNull(message = "é obrigatório") @Min(value = 0, message = "não pode ser negativa") Integer quantidadeDefeito) {
}
