package br.com.satc.manufatura.mapper;

import br.com.satc.manufatura.dto.OrdemProducaoRequestDTO;
import br.com.satc.manufatura.dto.OrdemProducaoResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import br.com.satc.manufatura.entity.Operador;
import br.com.satc.manufatura.entity.OrdemProducao;
import org.springframework.stereotype.Component;

@Component
public class OrdemProducaoMapper {

    public OrdemProducao toEntity(OrdemProducaoRequestDTO dto, Maquina maquina, Operador operador) {
        return OrdemProducao.builder()
                .maquina(maquina)
                .operador(operador)
                .produto(dto.produto().trim())
                .quantidadePlanejada(dto.quantidadePlanejada())
                .build();
    }

    public OrdemProducaoResponseDTO toResponseDTO(OrdemProducao ordem) {
        return new OrdemProducaoResponseDTO(ordem.getId(), ordem.getMaquina().getId(), ordem.getOperador().getId(),
                ordem.getProduto(), ordem.getQuantidadePlanejada(), ordem.getQuantidadeProduzida(),
                ordem.getQuantidadeDefeito(), ordem.getStatus(), ordem.getDataInicio(), ordem.getDataFim());
    }
}
