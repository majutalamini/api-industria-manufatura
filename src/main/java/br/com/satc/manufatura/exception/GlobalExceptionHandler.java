package br.com.satc.manufatura.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Centraliza o tratamento de exceções da API, convertendo erros conhecidos em respostas HTTP padronizadas para facilitar 
 * o entendimento pelo cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocio(RegraNegocioException ex, HttpServletRequest request) {
        // Retorna 400 quando uma operação não pode ser realizada devido a uma regra de negócio não atendida.
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        // Retorna 404 quando o recurso solicitado não é encontrado.
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Converte os erros de validação em uma lista com o nome do campo e a mensagem correspondente, facilitando a 
        // correção dos dados enviados pelo cliente.
        List<ErroResponse.CampoErro> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErroResponse.CampoErro(e.getField(), e.getDefaultMessage()))
                .toList();
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos", request, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarCorpoInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        // Trata corpos de requisição que não podem ser interpretados, por exemplo, quando o JSON está malformado ou um 
        // valor não pode ser convertido para o tipo esperado.
        return montar(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", request, List.of());
    }

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem, HttpServletRequest request,
                                                List<ErroResponse.CampoErro> campos) {
        // Centraliza a construção da resposta para manter o mesmo formato em todos os tratamentos de erro e registrar o 
        // caminho e o horário.
        ErroResponse corpo = new ErroResponse(status.value(), status.getReasonPhrase(), mensagem,
                request.getRequestURI(), LocalDateTime.now(), campos);
        // Define explicitamente o status HTTP e o formato JSON para que o cliente receba uma resposta consistente.
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(corpo);
    }
}
