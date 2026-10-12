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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrdemProducaoServiceTest {

    private OrdemProducaoRepository ordemRepository;
    private MaquinaRepository maquinaRepository;
    private OperadorRepository operadorRepository;
    private OrdemProducaoService service;

    private Maquina maquina;
    private Operador operador;

    @BeforeEach
    void preparar() {
        ordemRepository = mock(OrdemProducaoRepository.class);
        maquinaRepository = mock(MaquinaRepository.class);
        operadorRepository = mock(OperadorRepository.class);
        service = new OrdemProducaoService(ordemRepository, maquinaRepository, operadorRepository,
                new OrdemProducaoMapper());

        maquina = Maquina.builder().id(1L).codigo("TOR-01").nome("Torno").setor("Usinagem").build();
        operador = Operador.builder().id(1L).nome("Carlos").matricula("OP-001").build();

        when(maquinaRepository.findById(1L)).thenReturn(Optional.of(maquina));
        when(operadorRepository.findById(1L)).thenReturn(Optional.of(operador));
        when(ordemRepository.save(any(OrdemProducao.class))).thenAnswer(i -> i.getArgument(0));
    }

    private OrdemProducaoRequestDTO pedido() {
        return new OrdemProducaoRequestDTO(1L, 1L, "Eixo", 100);
    }

    private OrdemProducao ordemEmAndamento() {
        maquina.setStatus(StatusMaquina.EM_PRODUCAO);
        OrdemProducao ordem = OrdemProducao.builder().id(5L).maquina(maquina).operador(operador)
                .produto("Eixo").quantidadePlanejada(100).build();
        when(ordemRepository.findById(5L)).thenReturn(Optional.of(ordem));
        return ordem;
    }

    @Test
    void abrirOrdemColocaMaquinaEmProducao() {
        OrdemProducaoResponseDTO resposta = service.abrir(pedido());

        assertEquals(StatusOrdemProducao.EM_ANDAMENTO, resposta.status());
        assertEquals(StatusMaquina.EM_PRODUCAO, maquina.getStatus());
    }

    @Test
    void r1_maquinaIndisponivelNaoAbreOrdem() {
        maquina.setStatus(StatusMaquina.EM_MANUTENCAO);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.abrir(pedido()));

        assertEquals("R1: a máquina não está disponível", ex.getMessage());
    }

    @Test
    void r2_operadorComOrdemEmAndamentoNaoAbreOutra() {
        when(ordemRepository.existsByOperadorIdAndStatus(1L, StatusOrdemProducao.EM_ANDAMENTO)).thenReturn(true);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.abrir(pedido()));

        assertEquals("R2: o operador já possui uma ordem em andamento", ex.getMessage());
        assertEquals(StatusMaquina.DISPONIVEL, maquina.getStatus());
    }

    @Test
    void maquinaInexistenteLancaNaoEncontrado() {
        when(maquinaRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.abrir(new OrdemProducaoRequestDTO(9L, 1L, "Eixo", 10)));
    }

    @Test
    void r3_defeitoAcimaDeDezPorCentoReprovaEMandaParaManutencao() {
        ordemEmAndamento();

        OrdemProducaoResponseDTO resposta = service.finalizar(5L, new FinalizarOrdemRequestDTO(100, 11));

        assertEquals(StatusOrdemProducao.REPROVADA, resposta.status());
        assertEquals(StatusMaquina.EM_MANUTENCAO, maquina.getStatus());
    }

    @Test
    void r3_defeitoExatamenteDezPorCentoAprovaEMaquinaFicaDisponivel() {
        ordemEmAndamento();

        OrdemProducaoResponseDTO resposta = service.finalizar(5L, new FinalizarOrdemRequestDTO(100, 10));

        assertEquals(StatusOrdemProducao.APROVADA, resposta.status());
        assertEquals(StatusMaquina.DISPONIVEL, maquina.getStatus());
    }

    @Test
    void defeitoMaiorQueProduzidoEhRejeitado() {
        ordemEmAndamento();

        assertThrows(RegraNegocioException.class,
                () -> service.finalizar(5L, new FinalizarOrdemRequestDTO(10, 11)));
    }

    @Test
    void naoFinalizaOrdemJaFinalizada() {
        OrdemProducao ordem = ordemEmAndamento();
        ordem.setStatus(StatusOrdemProducao.APROVADA);

        assertThrows(RegraNegocioException.class,
                () -> service.finalizar(5L, new FinalizarOrdemRequestDTO(10, 0)));
    }
}
