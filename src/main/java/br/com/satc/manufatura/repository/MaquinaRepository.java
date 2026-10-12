package br.com.satc.manufatura.repository;

import br.com.satc.manufatura.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    boolean existsByCodigo(String codigo);
}
