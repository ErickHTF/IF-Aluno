package com.demo.aluno.dto;

import com.demo.aluno.model.Aluno;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AlunoDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "Nome do aluno (3 a 100 caracteres)", example = "João Silva")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Schema(description = "Email do aluno (formato válido e único)", example = "email@aluno.ifsp.edu.br", format = "email")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Matrícula do aluno (única)", example = "SP123456")
    @NotBlank(message = "Matrícula é obrigatória")
    private String matricula;

    @Schema(description = "CPF no formato XXX.XXX.XXX-XX", example = "358.823.769-41")
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @Schema(description = "Altura em metros (maior que zero)", example = "1.75")
    @NotNull(message = "Altura é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "Altura deve ser maior que zero")
    private Float altura;

    @Schema(description = "Telefone do aluno", example = "(16) 99123-4567")
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$", message = "Telefone inválido")
    private String telefone;

    @Schema(description = "Data de nascimento", example = "2005-03-15")
    private LocalDate dtNasc;

    @Schema(description = "Indica se o aluno está ativo", example = "true")
    private Boolean ativo;

    public AlunoDTO() {}

    public AlunoDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.email = aluno.getEmail();
        this.matricula = aluno.getMatricula();
        this.cpf = aluno.getCpf();
        this.altura = aluno.getAltura();
        this.telefone = aluno.getTelefone();
        this.dtNasc = aluno.getDtNasc();
        this.ativo = aluno.getAtivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Float getAltura() {
        return altura;
    }

    public void setAltura(Float altura) {
        this.altura = altura;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(LocalDate dtNasc) {
        this.dtNasc = dtNasc;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}