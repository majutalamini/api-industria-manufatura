package br.com.satc.manufatura.entity;

/**
 * Define os estados possíveis de uma máquina no fluxo de produção, permitindo representar sua situação atual de forma 
 * padronizada.
 */
public enum StatusMaquina {
    DISPONIVEL,
    EM_PRODUCAO,
    EM_MANUTENCAO
}
