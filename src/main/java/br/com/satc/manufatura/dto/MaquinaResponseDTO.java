package br.com.satc.manufatura.dto;

import br.com.satc.manufatura.entity.StatusMaquina;

public record MaquinaResponseDTO(Long id, String codigo, String nome, String setor, StatusMaquina status) {
}
