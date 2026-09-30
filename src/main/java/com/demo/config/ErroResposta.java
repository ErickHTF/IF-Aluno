package com.demo.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String mensagem,
        List<String> mensagens,
        Map<String, String> campos) {

    public static ErroResposta of(int status, String mensagem, List<String> mensagens, Map<String, String> campos) {
        return new ErroResposta(LocalDateTime.now(), status, mensagem, mensagens, campos);
    }
}
