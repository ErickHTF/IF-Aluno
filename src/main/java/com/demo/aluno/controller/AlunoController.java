package com.demo.aluno.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.aluno.dto.AlunoDTO;
import com.demo.aluno.model.Aluno;
import com.demo.aluno.service.AlunoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Tag(name = "aluno-api", description = "API para manter alunos.")
@Log4j2
@RequestMapping(path = "/demo-api")
@RestController
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Operation(summary = "Recuperar alunos.", description = "Retorna uma coleção de alunos.")
    @GetMapping("/alunos")
    public ResponseEntity<List<AlunoDTO>> getAll() {

        log.info("getAll()");

        return ResponseEntity.ok(
                alunoService.getAll().stream().map(AlunoDTO::new).toList());
    }

    @Operation(summary = "Recuperar aluno por Id.", description = "Retorna um aluno.")
    @GetMapping("/alunos/{id}")
    public ResponseEntity<AlunoDTO> getById(@PathVariable Long id) {

        log.info("getById( Id " + id + " )");

        return alunoService.findById(id)
                .map(a -> ResponseEntity.ok(new AlunoDTO(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Recuperar alunos por filtro.", description = "Retorna uma lista de alunos.")
    @GetMapping("/aluno")
    public ResponseEntity<List<AlunoDTO>> getAluno(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String matricula) {

        log.info("getAluno( " + id + ", " + nome + ", " + email + ", " + matricula + " )");

        List<Aluno> alunos = alunoService.searchAluno(id, nome, email, matricula);

        return ResponseEntity.ok(
                alunos.stream().map(AlunoDTO::new).toList());
    }

    @Operation(summary = "Criar aluno.", description = "Retorna o objeto aluno criado.")
    @PostMapping(path = "/alunos")
    public ResponseEntity<AlunoDTO> create(@Valid @RequestBody AlunoDTO alunoDTO) {

        log.info("create( " + alunoDTO + " )");

        Aluno a = alunoService.saveAluno(alunoDTO);

        return new ResponseEntity<>(new AlunoDTO(a), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar aluno.", description = "Retorna uma mensagem.")
    @PutMapping("/alunos/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody AlunoDTO alunoDTO, @PathVariable Long id) {

        log.info("update( " + alunoDTO + ", Id " + id + " )");

        if (alunoService.updateAluno(id, alunoDTO).isPresent()) {
            return new ResponseEntity<>("Aluno alterado com sucesso!", HttpStatus.OK);
        }

        return new ResponseEntity<>("Não foi possível encontrar o Aluno.", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Exclui um aluno por Id.", description = "Retorna uma mensagem.")
    @DeleteMapping("/alunos/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        log.info("delete( Id " + id + " )");

        try {
            alunoService.deleteById(id);

            return new ResponseEntity<>("Aluno excluído com sucesso!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Não foi possível excluir o Aluno.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
