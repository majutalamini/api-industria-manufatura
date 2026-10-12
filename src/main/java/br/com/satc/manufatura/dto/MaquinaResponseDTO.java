package br.com.satc.manufatura.dto;

import br.com.satc.manufatura.entity.StatusMaquina;

/**
 * Define os dados de máquina devolvidos pela API.
 *
 * Separa o formato da resposta da estrutura interna da entidade, permitindo controlar quais informações são 
 * disponibilizadas ao cliente.
 */
public record MaquinaResponseDTO(
    Long id, 
    String codigo, 
    String nome, 
    String setor, 
    // Informa a situação atual da máquina por meio de um tipo definido no sistema, evitando representar o status como 
    // texto livre.
    StatusMaquina status
    ) {
}
