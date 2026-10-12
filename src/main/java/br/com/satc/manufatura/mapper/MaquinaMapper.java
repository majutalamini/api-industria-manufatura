package br.com.satc.manufatura.mapper;

import br.com.satc.manufatura.dto.MaquinaRequestDTO;
import br.com.satc.manufatura.dto.MaquinaResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import org.springframework.stereotype.Component;

/**
 * Responsável por converter os dados entre os DTOs e a entidade Máquina, mantendo a transformação dos objetos separada 
 * das regras de negócio.
 */
@Component
public class MaquinaMapper {

    public Maquina toEntity(MaquinaRequestDTO dto) {
        // Remove espaços no início e no fim dos textos recebidos antes de utilizá-los na criação da entidade.
        return Maquina.builder()
                .codigo(dto.codigo().trim())
                .nome(dto.nome().trim())
                .setor(dto.setor().trim())
                .build();
    }

    public MaquinaResponseDTO toResponseDTO(Maquina maquina) {
        // Expõe os dados necessários para a resposta da API sem retornar diretamente a entidade persistida.
        return new MaquinaResponseDTO(maquina.getId(), maquina.getCodigo(), maquina.getNome(),
                maquina.getSetor(), maquina.getStatus());
    }
}
