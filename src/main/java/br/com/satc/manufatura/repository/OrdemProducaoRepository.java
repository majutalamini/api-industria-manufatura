package br.com.satc.manufatura.repository;

import br.com.satc.manufatura.entity.OrdemProducao;
import br.com.satc.manufatura.entity.StatusOrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de acesso aos dados das ordens de produção.
 *
 * O Spring Data JPA fornece as operações básicas de persistência e permite criar consultas a partir 
 * dos nomes dos métodos.
 */
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, Long> {

    // Verifica se existe uma ordem de produção associada ao operador com o status informado, 
    // permitindo validar regras de negócio, como impedir que um operador tenha mais de uma ordem em 
    // andamento.
    boolean existsByOperadorIdAndStatus(Long operadorId, StatusOrdemProducao status);
}
