package br.com.satc.manufatura.exception;

/**
 * Representa uma situação em que um recurso solicitado não foi encontrado, como uma máquina, um operador ou uma ordem de 
 * produção.
 *
 * Permite diferenciar esse cenário de outros erros da aplicação e tratá-lo centralmente no GlobalExceptionHandler.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    // Encaminha a mensagem recebida para a classe base, permitindo descrever qual recurso não foi encontrado.
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
