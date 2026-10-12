package br.com.satc.manufatura.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        String caminho,
        LocalDateTime timestamp,
        List<CampoErro> campos) {

    public record CampoErro(String campo, String mensagem) {
    }
}
