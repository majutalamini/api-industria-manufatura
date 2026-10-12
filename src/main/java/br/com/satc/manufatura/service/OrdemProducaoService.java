package br.com.satc.manufatura.service;

import br.com.satc.manufatura.dto.FinalizarOrdemRequestDTO;
import br.com.satc.manufatura.dto.OrdemProducaoRequestDTO;
import br.com.satc.manufatura.dto.OrdemProducaoResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import br.com.satc.manufatura.entity.Operador;
import br.com.satc.manufatura.entity.OrdemProducao;
import br.com.satc.manufatura.entity.StatusMaquina;
import br.com.satc.manufatura.entity.StatusOrdemProducao;
import br.com.satc.manufatura.exception.RecursoNaoEncontradoException;
import br.com.satc.manufatura.exception.RegraNegocioException;
import br.com.satc.manufatura.mapper.OrdemProducaoMapper;
import br.com.satc.manufatura.repository.MaquinaRepository;
import br.com.satc.manufatura.repository.OperadorRepository;
import br.com.satc.manufatura.repository.OrdemProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrdemProducaoService {

    private static final double LIMITE_DEFEITO = 0.10;

    private final OrdemProducaoRepository ordemRepository;
    private final MaquinaRepository maquinaRepository;
    private final OperadorRepository operadorRepository;
    private final OrdemProducaoMapper ordemMapper;

    @Transactional
    public OrdemProducaoResponseDTO abrir(OrdemProducaoRequestDTO dto) {
        Maquina maquina = maquinaRepository.findById(dto.maquinaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Máquina não encontrada: " + dto.maquinaId()));
        Operador operador = operadorRepository.findById(dto.operadorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado: " + dto.operadorId()));

        // R1: só abre ordem para máquina disponível
        if (maquina.getStatus() != StatusMaquina.DISPONIVEL) {
            throw new RegraNegocioException("R1: a máquina não está disponível");
        }
        // R2: um operador só pode ter uma ordem em andamento
        if (ordemRepository.existsByOperadorIdAndStatus(operador.getId(), StatusOrdemProducao.EM_ANDAMENTO)) {
            throw new RegraNegocioException("R2: o operador já possui uma ordem em andamento");
        }

        maquina.setStatus(StatusMaquina.EM_PRODUCAO);
        maquinaRepository.save(maquina);

        OrdemProducao salva = ordemRepository.save(ordemMapper.toEntity(dto, maquina, operador));
        return ordemMapper.toResponseDTO(salva);
    }

    @Transactional
    public OrdemProducaoResponseDTO finalizar(Long id, FinalizarOrdemRequestDTO dto) {
        OrdemProducao ordem = ordemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de produção não encontrada: " + id));

        if (ordem.getStatus() != StatusOrdemProducao.EM_ANDAMENTO) {
            throw new RegraNegocioException("Só é possível finalizar uma ordem em andamento");
        }
        if (dto.quantidadeDefeito() > dto.quantidadeProduzida()) {
            throw new RegraNegocioException("A quantidade com defeito não pode ser maior que a produzida");
        }

        ordem.setQuantidadeProduzida(dto.quantidadeProduzida());
        ordem.setQuantidadeDefeito(dto.quantidadeDefeito());
        ordem.setDataFim(LocalDateTime.now());

        // R3: defeito acima de 10% reprova a ordem e manda a máquina para manutenção
        Maquina maquina = ordem.getMaquina();
        if (dto.quantidadeDefeito() > dto.quantidadeProduzida() * LIMITE_DEFEITO) {
            ordem.setStatus(StatusOrdemProducao.REPROVADA);
            maquina.setStatus(StatusMaquina.EM_MANUTENCAO);
        } else {
            ordem.setStatus(StatusOrdemProducao.APROVADA);
            maquina.setStatus(StatusMaquina.DISPONIVEL);
        }
        maquinaRepository.save(maquina);

        return ordemMapper.toResponseDTO(ordemRepository.save(ordem));
    }
}
