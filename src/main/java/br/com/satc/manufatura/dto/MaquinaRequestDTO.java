package br.com.satc.manufatura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Define os dados recebidos para cadastrar uma máquina.
 *
 * As validações garantem que os campos obrigatórios sejam preenchidos e respeitem os limites de caracteres definidos 
 * para cada informação.
 */
public record MaquinaRequestDTO(
        // Impede códigos vazios ou compostos apenas por espaços, limitando o tamanho para atender à regra definida 
        // para o cadastro.
        @NotBlank(message = "é obrigatório") @Size(max = 20, message = "deve ter no máximo 20 caracteres") String codigo,
        // Exige um nome válido e limita seu tamanho para evitar valores maiores do que o permitido pelo cadastro.
        @NotBlank(message = "é obrigatório") @Size(max = 100, message = "deve ter no máximo 100 caracteres") String nome,
        // Exige a identificação do setor e limita a quantidade de caracteres aceita nesse campo.
        @NotBlank(message = "é obrigatório") @Size(max = 50, message = "deve ter no máximo 50 caracteres") String setor) {
}
