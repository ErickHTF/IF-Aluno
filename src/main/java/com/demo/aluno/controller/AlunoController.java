package com.demo.aluno.controller;

import com.demo.aluno.dto.AlunoDTO;
import com.demo.aluno.model.Aluno;
import com.demo.aluno.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/aluno")
    public ResponseEntity<List<AlunoDTO>> getAluno(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String matricula) {

        List<Aluno> alunos = alunoService.searchAluno(id, nome, email, matricula);

        return ResponseEntity.ok(
                alunos.stream().map(AlunoDTO::new).toList());
    }
}