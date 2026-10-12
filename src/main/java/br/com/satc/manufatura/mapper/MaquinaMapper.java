package br.com.satc.manufatura.mapper;

import br.com.satc.manufatura.dto.MaquinaRequestDTO;
import br.com.satc.manufatura.dto.MaquinaResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import org.springframework.stereotype.Component;

@Component
public class MaquinaMapper {

    public Maquina toEntity(MaquinaRequestDTO dto) {
        return Maquina.builder()
                .codigo(dto.codigo().trim())
                .nome(dto.nome().trim())
                .setor(dto.setor().trim())
                .build();
    }

    public MaquinaResponseDTO toResponseDTO(Maquina maquina) {
        return new MaquinaResponseDTO(maquina.getId(), maquina.getCodigo(), maquina.getNome(),
                maquina.getSetor(), maquina.getStatus());
    }
}
