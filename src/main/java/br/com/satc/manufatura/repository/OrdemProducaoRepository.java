package br.com.satc.manufatura.repository;

import br.com.satc.manufatura.entity.OrdemProducao;
import br.com.satc.manufatura.entity.StatusOrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, Long> {

    boolean existsByOperadorIdAndStatus(Long operadorId, StatusOrdemProducao status);
}
