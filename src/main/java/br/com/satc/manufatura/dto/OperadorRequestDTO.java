package br.com.satc.manufatura.dto;

import br.com.satc.manufatura.entity.TurnoOperador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Define os dados recebidos para cadastrar um operador.
 *
 * As validações garantem que os campos obrigatórios sejam preenchidos e respeitem os limites definidos.
 */
public record OperadorRequestDTO(
        // Exige um nome válido e limita o tamanho para não ultrapassar o tamanho permitido no banco.
        @NotBlank(message = "é obrigatório") @Size(max = 100, message = "deve ter no máximo 100 caracteres") String nome,
        // Exige a matrícula e limita o tamanho para garantir o padrão de identificação.
        @NotBlank(message = "é obrigatório") @Size(max = 20, message = "deve ter no máximo 20 caracteres") String matricula,
        // Exige a seleção de um dos turnos válidos do sistema.
        @NotNull(message = "é obrigatório") TurnoOperador turno) {
}
