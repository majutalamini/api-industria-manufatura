package br.com.satc.manufatura.service;

import br.com.satc.manufatura.dto.MaquinaRequestDTO;
import br.com.satc.manufatura.dto.MaquinaResponseDTO;
import br.com.satc.manufatura.entity.Maquina;
import br.com.satc.manufatura.entity.StatusMaquina;
import br.com.satc.manufatura.exception.RecursoNaoEncontradoException;
import br.com.satc.manufatura.exception.RegraNegocioException;
import br.com.satc.manufatura.mapper.MaquinaMapper;
import br.com.satc.manufatura.repository.MaquinaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaquinaServiceTest {

    private MaquinaRepository maquinaRepository;
    private MaquinaService service;

    private Maquina maquina;

    @BeforeEach
    void preparar() {
        maquinaRepository = mock(MaquinaRepository.class);
        service = new MaquinaService(maquinaRepository, new MaquinaMapper());

        maquina = Maquina.builder()
                .id(1L)
                .codigo("TOR-01")
                .nome("Torno CNC")
                .setor("Usinagem")
                .status(StatusMaquina.DISPONIVEL)
                .build();

        when(maquinaRepository.findById(1L)).thenReturn(Optional.of(maquina));
        when(maquinaRepository.save(any(Maquina.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void criarMaquinaComSucesso() {
        when(maquinaRepository.existsByCodigo("TOR-02")).thenReturn(false);

        MaquinaResponseDTO resposta = service.criar(new MaquinaRequestDTO("TOR-02", "Fresa", "Usinagem"));

        assertEquals("TOR-02", resposta.codigo());
        assertEquals(StatusMaquina.DISPONIVEL, resposta.status());
    }

    @Test
    void naoCriaMaquinaComCodigoDuplicado() {
        when(maquinaRepository.existsByCodigo("TOR-01")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> service.criar(new MaquinaRequestDTO("TOR-01", "Torno", "Usinagem")));
    }

    @Test
    void liberarMaquinaEmManutencaoPassaParaDisponivel() {
        maquina.setStatus(StatusMaquina.EM_MANUTENCAO);

        MaquinaResponseDTO resposta = service.liberar(1L);

        assertEquals(StatusMaquina.DISPONIVEL, resposta.status());
        assertEquals(StatusMaquina.DISPONIVEL, maquina.getStatus());
    }

    @Test
    void naoLiberaMaquinaQueNaoEstaEmManutencao() {
        maquina.setStatus(StatusMaquina.DISPONIVEL);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.liberar(1L));
        assertEquals("A máquina não está em manutenção", ex.getMessage());
    }

    @Test
    void liberarMaquinaInexistenteLancaExcecao() {
        when(maquinaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.liberar(99L));
    }
}
