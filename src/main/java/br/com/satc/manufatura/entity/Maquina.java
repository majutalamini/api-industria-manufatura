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
 * Representa uma máquina do sistema e seu mapeamento para a tabela "maquinas".
 *
 * As anotações JPA definem como os dados são persistidos, enquanto o Lombok reduz a necessidade de métodos e 
 * construtores escritos manualmente.
 */
@Entity
@Table(name = "maquinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O código é obrigatório e único para permitir a identificação de cada máquina sem duplicidades no banco de dados.
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    // Define o nome da máquina, que deve ser informado e respeitar o limite de 100 caracteres no banco.
    @Column(nullable = false, length = 100)
    private String nome;

    // Armazena o setor ao qual a máquina pertence, com limite de 50 caracteres e sem permitir valor nulo.
    @Column(nullable = false, length = 50)
    private String setor;

    // Armazena o status como texto no banco, em vez de usar a posição numérica do valor no enum.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusMaquina status = StatusMaquina.DISPONIVEL;
}
