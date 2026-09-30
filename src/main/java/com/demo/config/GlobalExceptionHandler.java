package com.demo.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidation(MethodArgumentNotValidException ex) {

        log.warn("Erro de validação: " + ex.getMessage());

        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            campos.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(ErroResposta.of(
                HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos. Corrija os campos destacados e tente novamente.",
                new ArrayList<>(campos.values()),
                campos));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResposta> handleConstraint(ConstraintViolationException ex) {

        log.warn("Erro de validação de parâmetro: " + ex.getMessage());

        Map<String, String> campos = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            campos.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return ResponseEntity.badRequest().body(ErroResposta.of(
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetros inválidos na requisição.",
                new ArrayList<>(campos.values()),
                campos));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> handleNotReadable(HttpMessageNotReadableException ex) {

        log.warn("Erro ao ler o corpo da requisição: " + ex.getMessage());

        return ResponseEntity.badRequest().body(ErroResposta.of(
                HttpStatus.BAD_REQUEST.value(),
                "Não foi possível ler os dados enviados. Verifique o formato dos campos e tente novamente.",
                List.of("Corpo da requisição inválido ou com formato inesperado."),
                Map.of()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        String mensagem = "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue();

        log.warn("Erro de conversão de parâmetro: " + mensagem);

        return ResponseEntity.badRequest().body(ErroResposta.of(
                HttpStatus.BAD_REQUEST.value(),
                mensagem,
                List.of(mensagem),
                Map.of(ex.getName(), mensagem)));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResposta> handleDataIntegrity(DataIntegrityViolationException ex) {

        String detail = rootMessage(ex);
        String mensagem = mensagemDuplicidade(detail);

        log.warn("Erro de integridade: " + detail);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErroResposta.of(
                HttpStatus.CONFLICT.value(),
                mensagem,
                List.of(mensagem),
                Map.of()));
    }

    private String mensagemDuplicidade(String detail) {
        if (detail == null) {
            return "Registro já cadastrado.";
        }
        if (detail.contains("matricula")) {
            return "Matrícula já cadastrada.";
        }
        if (detail.contains("email")) {
            return "Email já cadastrado.";
        }
        if (detail.contains("cpf")) {
            return "CPF já cadastrado.";
        }
        return "Registro já cadastrado.";
    }

    private String rootMessage(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage();
    }
}
