package com.demo.escola.dto;

import com.demo.escola.model.Escola;
import com.demo.escola.model.Nivel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EscolaDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(description = "Nome da escola (3 a 100 caracteres)", example = "IFSP")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Schema(description = "Nível de ensino", example = "SUPERIOR")
    @NotNull(message = "Nível é obrigatório")
    private Nivel nivel;

    public EscolaDTO() {}

    public EscolaDTO(Escola escola) {
        this.id = escola.getId();
        this.nome = escola.getNome();
        this.nivel = escola.getNivel();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }
}
