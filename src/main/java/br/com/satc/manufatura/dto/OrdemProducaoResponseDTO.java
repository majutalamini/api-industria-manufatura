package br.com.satc.manufatura.dto;

import br.com.satc.manufatura.entity.StatusOrdemProducao;

import java.time.LocalDateTime;

/**
 * Define os dados de uma ordem de produção retornados pela API.
 *
 * Separa as informações disponibilizadas ao cliente da estrutura interna da entidade, permitindo controlar os dados 
 * expostos nas respostas.
 */
public record OrdemProducaoResponseDTO(
        Long id,
        // Identificadores permitem consultar a máquina e o operador relacionados à ordem sem retornar as entidades 
        // completas.
        Long maquinaId,
        Long operadorId,
        String produto,
        Integer quantidadePlanejada,
        // Quantidades registram o resultado da produção e os itens defeituosos ao longo da execução da ordem.
        Integer quantidadeProduzida,
        Integer quantidadeDefeito,
        // Representa a situação da ordem por meio dos estados definidos no sistema, mantendo os status padronizados.
        StatusOrdemProducao status,
        // Registra quando a produção começou e quando foi finalizada.
        LocalDateTime dataInicio,
        // A data de término pode ser nula enquanto a ordem estiver em andamento.
        LocalDateTime dataFim) {
}
