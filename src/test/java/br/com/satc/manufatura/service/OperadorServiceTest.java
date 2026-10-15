package br.com.satc.manufatura.service;

import br.com.satc.manufatura.dto.OperadorRequestDTO;
import br.com.satc.manufatura.dto.OperadorResponseDTO;
import br.com.satc.manufatura.entity.Operador;
import br.com.satc.manufatura.entity.TurnoOperador;
import br.com.satc.manufatura.exception.RecursoNaoEncontradoException;
import br.com.satc.manufatura.exception.RegraNegocioException;
import br.com.satc.manufatura.mapper.OperadorMapper;
import br.com.satc.manufatura.repository.OperadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperadorServiceTest {

    private OperadorRepository operadorRepository;
    private OperadorService service;

    private Operador operador;

    @BeforeEach
    void preparar() {
        operadorRepository = mock(OperadorRepository.class);
        service = new OperadorService(operadorRepository, new OperadorMapper());

        operador = Operador.builder()
                .id(1L)
                .nome("Carlos Souza")
                .matricula("OP-001")
                .turno(TurnoOperador.MANHA)
                .build();

        when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));
        when(operadorRepository.save(any(Operador.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void criarOperadorComSucesso() {
        when(operadorRepository.existsByMatricula("OP-002")).thenReturn(false);

        OperadorResponseDTO resposta = service.criar(new OperadorRequestDTO("Ana Lima", "OP-002", TurnoOperador.TARDE));

        assertEquals("Ana Lima", resposta.nome());
        assertEquals("OP-002", resposta.matricula());
        assertEquals(TurnoOperador.TARDE, resposta.turno());
    }

    @Test
    void naoCriaOperadorComMatriculaDuplicada() {
        when(operadorRepository.existsByMatricula("OP-001")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> service.criar(new OperadorRequestDTO("Carlos", "OP-001", TurnoOperador.MANHA)));
    }

    @Test
    void listarOperadoresRetornaLista() {
        when(operadorRepository.findAll()).thenReturn(List.of(operador));

        List<OperadorResponseDTO> lista = service.listar();

        assertEquals(1, lista.size());
        assertEquals("Carlos Souza", lista.get(0).nome());
    }

    @Test
    void buscarPorIdExistenteRetornaOperador() {
        OperadorResponseDTO resposta = service.buscarPorId(1L);

        assertEquals(1L, resposta.id());
        assertEquals("OP-001", resposta.matricula());
    }

    @Test
    void buscarPorIdInexistenteLancaExcecao() {
        when(operadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(99L));
    }
}
