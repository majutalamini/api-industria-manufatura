package br.com.satc.manufatura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Define os dados recebidos para abrir uma ordem de produção.
 *
 * As validações garantem o preenchimento dos campos obrigatórios e estabelecem limites para os dados informados na 
 * requisição.
 */
public record OrdemProducaoRequestDTO(
        // Identifica a máquina associada à ordem.
        // A existência e a disponibilidade da máquina devem ser verificadas na camada de serviço.
        @NotNull(message = "é obrigatório") Long maquinaId,
        // Identifica o operador responsável pela ordem.
        // A existência e a validade desse operador também devem ser verificadas na camada de serviço.
        @NotNull(message = "é obrigatório") Long operadorId,
        // Exige a descrição do produto e limita seu tamanho para respeitar o formato previsto para o cadastro.
        @NotBlank(message = "é obrigatório") @Size(max = 100, message = "deve ter no máximo 100 caracteres") String produto,
        // Exige uma quantidade planejada maior que zero, pois uma ordem de produção precisa definir um volume a produzir.
        @NotNull(message = "é obrigatório") @Positive(message = "deve ser maior que zero") Integer quantidadePlanejada) {
}
