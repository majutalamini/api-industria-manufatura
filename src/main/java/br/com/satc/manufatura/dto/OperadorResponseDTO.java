package br.com.satc.manufatura.dto;

import br.com.satc.manufatura.entity.TurnoOperador;

/**
 * Define os dados de operador devolvidos pela API.
 *
 * Separa a representação de saída da entidade interna de persistência.
 */
public record OperadorResponseDTO(
        Long id,
        String nome,
        String matricula,
        TurnoOperador turno) {
}
