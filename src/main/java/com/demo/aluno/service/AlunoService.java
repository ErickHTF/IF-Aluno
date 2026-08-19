package com.demo.aluno.service;

import com.demo.aluno.model.Aluno;
import com.demo.aluno.model.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository aRepository;

    public void saveAluno(Aluno aluno) {
        aRepository.save(aluno);
    }

    public List<Aluno> searchAluno(Integer id, String nome, String email, String matricula) {
        return aRepository.searchAll(id, nome, email, matricula);
    }
}