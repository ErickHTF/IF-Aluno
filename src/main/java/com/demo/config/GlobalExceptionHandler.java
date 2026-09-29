package com.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        log.warn("Erro de validação: " + ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {

        String detail = rootMessage(ex);

        log.warn("Erro de integridade: " + detail);

        Map<String, String> body = new HashMap<>();
        body.put("erro", mensagemDuplicidade(detail));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
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
