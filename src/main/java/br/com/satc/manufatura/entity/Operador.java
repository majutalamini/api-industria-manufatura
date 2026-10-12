package br.com.satc.manufatura.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um operador da fábrica e seus dados persistidos na tabela "operadores".
 *
 * O mapeamento JPA define como as informações do operador são armazenadas no banco de dados.
 */
@Entity
@Table(name = "operadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Armazena o nome do operador, que é obrigatório e possui limite de 100 caracteres no banco.
    @Column(nullable = false, length = 100)
    private String nome;

    // A matrícula identifica o operador e deve ser única, evitando duplicidades no cadastro.
    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    // Armazena o turno como texto, utilizando os valores definidos no enum TurnoOperador.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TurnoOperador turno;
}
