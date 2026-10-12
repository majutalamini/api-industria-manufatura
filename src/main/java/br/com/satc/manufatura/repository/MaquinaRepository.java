package br.com.satc.manufatura.repository;

import br.com.satc.manufatura.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de acesso aos dados de máquinas.
 *
 * O Spring Data JPA fornece a implementação das operações de persistência, evitando a necessidade de 
 * escrever consultas manualmente para as operações básicas.
 */
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    // Verifica se já existe uma máquina com o código informado, permitindo validar a unicidade antes 
    // de realizar o cadastro.
    boolean existsByCodigo(String codigo);
}
