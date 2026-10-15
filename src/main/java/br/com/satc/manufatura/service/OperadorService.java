package br.com.satc.manufatura.service;

import br.com.satc.manufatura.dto.OperadorRequestDTO;
import br.com.satc.manufatura.dto.OperadorResponseDTO;
import br.com.satc.manufatura.entity.Operador;
import br.com.satc.manufatura.exception.RecursoNaoEncontradoException;
import br.com.satc.manufatura.exception.RegraNegocioException;
import br.com.satc.manufatura.mapper.OperadorMapper;
import br.com.satc.manufatura.repository.OperadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contém as operações e regras relacionadas a operadores, mantendo controller e persistência separados.
 */
@Service
@RequiredArgsConstructor
public class OperadorService {

    private final OperadorRepository operadorRepository;
    private final OperadorMapper operadorMapper;

    @Transactional
    public OperadorResponseDTO criar(OperadorRequestDTO dto) {
        String matricula = dto.matricula().trim();
        if (operadorRepository.existsByMatricula(matricula)) {
            throw new RegraNegocioException("Já existe um operador com a matrícula " + matricula);
        }
        Operador salvo = operadorRepository.save(operadorMapper.toEntity(dto));
        return operadorMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<OperadorResponseDTO> listar() {
        return operadorRepository.findAll().stream().map(operadorMapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public OperadorResponseDTO buscarPorId(Long id) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Operador não encontrado: " + id));
        return operadorMapper.toResponseDTO(operador);
    }
}
