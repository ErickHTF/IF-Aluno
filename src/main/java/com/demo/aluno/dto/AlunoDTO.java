package com.demo.aluno.dto;

import com.demo.aluno.model.Aluno;

import java.time.LocalDate;

public class AlunoDTO {

    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private String cpf;
    private Float altura;
    private String telefone;
    private LocalDate dtNasc;
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