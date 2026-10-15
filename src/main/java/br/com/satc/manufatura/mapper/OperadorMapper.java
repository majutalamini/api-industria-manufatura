package br.com.satc.manufatura.mapper;

import br.com.satc.manufatura.dto.OperadorRequestDTO;
import br.com.satc.manufatura.dto.OperadorResponseDTO;
import br.com.satc.manufatura.entity.Operador;
import org.springframework.stereotype.Component;

/**
 * Responsável por converter os dados entre os DTOs e a entidade Operador.
 */
@Component
public class OperadorMapper {

    public Operador toEntity(OperadorRequestDTO dto) {
        // Remove espaços extras antes de persistir
        return Operador.builder()
                .nome(dto.nome().trim())
                .matricula(dto.matricula().trim())
                .turno(dto.turno())
                .build();
    }

    public OperadorResponseDTO toResponseDTO(Operador operador) {
        // Expõe apenas os dados necessários na resposta
        return new OperadorResponseDTO(
                operador.getId(),
                operador.getNome(),
                operador.getMatricula(),
                operador.getTurno()
        );
    }
}
