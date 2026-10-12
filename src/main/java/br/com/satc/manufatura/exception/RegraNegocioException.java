package br.com.satc.manufatura.exception;

/**
 * Representa uma situação em que uma operação não pode ser realizada porque uma regra de negócio do sistema não foi 
 * atendida.
 *
 * Permite identificar esse tipo de falha e tratá-la centralmente no GlobalExceptionHandler.
 */
public class RegraNegocioException extends RuntimeException {

    // Encaminha a mensagem para a classe base, permitindo descrever o motivo pelo qual a operação foi rejeitada.
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
