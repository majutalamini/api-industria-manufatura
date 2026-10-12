package br.com.satc.manufatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API de indústria e manufatura.
 *
 * A anotação @SpringBootApplication habilita a configuração automática do Spring Boot, a varredura de 
 * componentes e a configuração da aplicação.
 */
@SpringBootApplication
public class ApiIndustriaManufaturaApplication {

    /**
     * Método principal responsável por iniciar a aplicação.
     *
     * O Spring Boot inicializa o contexto da aplicação, registra os componentes identificados e inicia o 
     * servidor web incorporado para disponibilizar os endpoints da API.
     * 
     * @param args argumentos recebidos na inicialização.
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiIndustriaManufaturaApplication.class, args);
    }
}
