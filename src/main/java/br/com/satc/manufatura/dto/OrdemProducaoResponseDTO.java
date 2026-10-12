package br.com.satc.manufatura.dto;

import br.com.satc.manufatura.entity.StatusOrdemProducao;

import java.time.LocalDateTime;

public record OrdemProducaoResponseDTO(
        Long id,
        Long maquinaId,
        Long operadorId,
        String produto,
        Integer quantidadePlanejada,
        Integer quantidadeProduzida,
        Integer quantidadeDefeito,
        StatusOrdemProducao status,
        LocalDateTime dataInicio,
        LocalDateTime dataFim) {
}
