package com.demo.escola.controller;

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

import com.demo.escola.dto.EscolaDTO;
import com.demo.escola.model.Escola;
import com.demo.escola.model.Nivel;
import com.demo.escola.service.EscolaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Tag(name = "escola-api", description = "API para manter escolas.")
@Log4j2
@RequestMapping(path = "/demo-api")
@RestController
public class EscolaController {

    @Autowired
    private EscolaService escolaService;

    @Operation(summary = "Recuperar escolas.", description = "Retorna uma coleção de escolas.")
    @GetMapping("/escolas")
    public ResponseEntity<List<EscolaDTO>> getAll() {

        log.info("getAll()");

        return ResponseEntity.ok(
                escolaService.getAll().stream().map(EscolaDTO::new).toList());
    }

    @Operation(summary = "Recuperar escola por Id.", description = "Retorna uma escola.")
    @GetMapping("/escolas/{id}")
    public ResponseEntity<EscolaDTO> getById(@PathVariable Integer id) {

        log.info("getById( Id " + id + " )");

        return escolaService.findById(id)
                .map(e -> ResponseEntity.ok(new EscolaDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Recuperar escolas por filtro.", description = "Retorna uma lista de escolas.")
    @GetMapping("/escola")
    public ResponseEntity<List<EscolaDTO>> getEscola(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Nivel nivel) {

        log.info("getEscola( " + id + ", " + nome + ", " + nivel + " )");

        List<Escola> escolas = escolaService.searchEscola(id, nome, nivel);

        return ResponseEntity.ok(
                escolas.stream().map(EscolaDTO::new).toList());
    }

    @Operation(summary = "Criar escola.", description = "Retorna o objeto escola criado.")
    @PostMapping(path = "/escolas")
    public ResponseEntity<EscolaDTO> create(@Valid @RequestBody EscolaDTO escolaDTO) {

        log.info("create( " + escolaDTO + " )");

        Escola e = escolaService.saveEscola(escolaDTO);

        return new ResponseEntity<>(new EscolaDTO(e), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar escola.", description = "Retorna uma mensagem.")
    @PutMapping("/escolas/{id}")
    public ResponseEntity<String> update(@Valid @RequestBody EscolaDTO escolaDTO, @PathVariable Integer id) {

        log.info("update( " + escolaDTO + ", Id " + id + " )");

        if (escolaService.updateEscola(id, escolaDTO).isPresent()) {
            return new ResponseEntity<>("Escola alterada com sucesso!", HttpStatus.OK);
        }

        return new ResponseEntity<>("Não foi possível encontrar a Escola.", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Exclui uma escola por Id.", description = "Retorna uma mensagem.")
    @DeleteMapping("/escolas/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {

        log.info("delete( Id " + id + " )");

        try {
            escolaService.deleteById(id);

            return new ResponseEntity<>("Escola excluída com sucesso!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Não foi possível excluir a Escola.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
