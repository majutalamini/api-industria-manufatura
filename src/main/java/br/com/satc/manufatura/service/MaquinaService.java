package br.com.satc.manufatura.service;

import br.com.satc.manufatura.dto.MaquinaRequestDTO;
import br.com.satc.manufatura.dto.MaquinaResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import br.com.satc.manufatura.exception.RecursoNaoEncontradoException;
import br.com.satc.manufatura.exception.RegraNegocioException;
import br.com.satc.manufatura.mapper.MaquinaMapper;
import br.com.satc.manufatura.repository.MaquinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Descrição: Contém as operações e regras relacionadas a máquinas, mantendo controller e persistência separados.
 */
@Service
@RequiredArgsConstructor
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;
    private final MaquinaMapper maquinaMapper;

    @Transactional
    public MaquinaResponseDTO criar(MaquinaRequestDTO dto) {
        String codigo = dto.codigo().trim();
        if (maquinaRepository.existsByCodigo(codigo)) {
            throw new RegraNegocioException("Já existe uma máquina com o código " + codigo);
        }
        Maquina salva = maquinaRepository.save(maquinaMapper.toEntity(dto));
        return maquinaMapper.toResponseDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<MaquinaResponseDTO> listar() {
        return maquinaRepository.findAll().stream().map(maquinaMapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public MaquinaResponseDTO buscarPorId(Long id) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Máquina não encontrada: " + id));
        return maquinaMapper.toResponseDTO(maquina);
    }
}
