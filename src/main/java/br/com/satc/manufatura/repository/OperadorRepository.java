package br.com.satc.manufatura.repository;

import br.com.satc.manufatura.entity.Operador;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de acesso aos dados de operadores.
 *
 * O Spring Data JPA fornece a implementação das operações de persistência, evitando a necessidade de 
 * escrever consultas manualmente para as operações básicas.
 */
public interface OperadorRepository extends JpaRepository<Operador, Long> {

    // Verifica se já existe um operador cadastrado com a matrícula informada.
    boolean existsByMatricula(String matricula);
}
