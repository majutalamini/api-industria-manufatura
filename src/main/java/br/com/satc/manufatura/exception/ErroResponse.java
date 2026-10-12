package br.com.satc.manufatura.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Define o formato padronizado das respostas de erro da API, facilitando a identificação do problema e sua origem pelo 
 * cliente.
 */
public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        String caminho,
        LocalDateTime timestamp,
        // Permite retornar detalhes de validação de campos específicos quando uma requisição contém dados inválidos.
        List<CampoErro> campos) {

    /**
     * Representa um erro associado a um campo específico da requisição, permitindo informar qual campo falhou e o motivo 
     * da falha.
     */
    public record CampoErro(String campo, String mensagem) {
    }
}
