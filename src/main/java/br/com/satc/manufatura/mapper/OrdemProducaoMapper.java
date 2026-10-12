package br.com.satc.manufatura.mapper;

import br.com.satc.manufatura.dto.OrdemProducaoRequestDTO;
import br.com.satc.manufatura.dto.OrdemProducaoResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import br.com.satc.manufatura.entity.Operador;
import br.com.satc.manufatura.entity.OrdemProducao;
import org.springframework.stereotype.Component;

/**
 * Responsável por converter os dados entre os DTOs e a entidade OrdemProducao, mantendo essa 
 * transformação separada das regras de negócio.
 */
@Component
public class OrdemProducaoMapper {

    public OrdemProducao toEntity(OrdemProducaoRequestDTO dto, Maquina maquina, Operador operador) {
        // Associa à ordem as entidades recebidas, evitando que o mapper precise buscar máquina e 
        // operador diretamente no banco de dados.
        // Essa busca e a validação dos recursos devem ocorrer no serviço.
        return OrdemProducao.builder()
                .maquina(maquina)
                .operador(operador)
                .produto(dto.produto().trim())
                .quantidadePlanejada(dto.quantidadePlanejada())
                .build();
    }

    public OrdemProducaoResponseDTO toResponseDTO(OrdemProducao ordem) {
        // Retorna os identificadores dos relacionamentos, em vez de expor os objetos completos de 
        // máquina e operador na resposta da API.
        return new OrdemProducaoResponseDTO(ordem.getId(), ordem.getMaquina().getId(), ordem.getOperador().getId(),
                ordem.getProduto(), ordem.getQuantidadePlanejada(), ordem.getQuantidadeProduzida(),
                ordem.getQuantidadeDefeito(), ordem.getStatus(), ordem.getDataInicio(), ordem.getDataFim());
    }
}
