package br.com.satc.manufatura.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa uma ordem de produção, incluindo os recursos envolvidos, as quantidades planejadas e realizadas, o status 
 * e as datas da operação.
 *
 * O mapeamento JPA define como a ordem e seus relacionamentos são armazenados no banco de dados.
 */
@Entity
@Table(name = "ordens_producao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada ordem está associada a uma máquina.
    // O carregamento LAZY evita buscar os dados da máquina automaticamente em toda consulta da ordem, quando o 
    // relacionamento não for utilizado.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    // Associa a ordem ao operador responsável.
    // O relacionamento é obrigatório e também utiliza carregamento sob demanda.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operador_id", nullable = false)
    private Operador operador;

    // Identifica o produto que será fabricado.
    @Column(nullable = false, length = 100)
    private String produto;

    // Define a quantidade prevista para a produção da ordem.
    @Column(name = "quantidade_planejada", nullable = false)
    private Integer quantidadePlanejada;

    // Registra a quantidade efetivamente produzida durante a execução.
    // Pode permanecer nula até que o resultado seja informado.
    @Column(name = "quantidade_produzida")
    private Integer quantidadeProduzida;

    // Registra a quantidade de itens defeituosos identificados na produção.
    // Pode permanecer nula enquanto o resultado ainda não for registrado.
    @Column(name = "quantidade_defeito")
    private Integer quantidadeDefeito;

    // Armazena o status como texto para manter a representação associada aos nomes definidos no enum.
    // O Builder.Default preserva o valor inicial quando o Builder é utilizado sem informar outro status.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusOrdemProducao status = StatusOrdemProducao.EM_ANDAMENTO;

    // Registra o momento de início da ordem.
    // O valor padrão é definido no momento em que a instância é criada.
    @Column(name = "data_inicio", nullable = false)
    @Builder.Default
    private LocalDateTime dataInicio = LocalDateTime.now();

    // Registra o momento de conclusão da ordem.
    // Pode permanecer nulo enquanto a produção não for finalizada.
    @Column(name = "data_fim")
    private LocalDateTime dataFim;
}
