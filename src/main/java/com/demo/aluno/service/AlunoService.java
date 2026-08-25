package com.demo.aluno.service;

import com.demo.aluno.dto.AlunoDTO;
import com.demo.aluno.model.Aluno;
import com.demo.aluno.model.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository aRepository;

    public void saveAluno(AlunoDTO alunoDTO) {
        Aluno a = new Aluno();

        a.setEmail(alunoDTO.getEmail());
        a.setMatricula(alunoDTO.getMatricula());
        a.setNome(alunoDTO.getNome());
        a.setCpf(alunoDTO.getCpf());
        a.setAltura(alunoDTO.getAltura());
        a.setTelefone(alunoDTO.getTelefone());
        a.setDtNasc(alunoDTO.getDtNasc());
        a.setAtivo(alunoDTO.getAtivo());

        a = aRepository.save(a);
    }

    public List<Aluno> searchAluno(Integer id, String nome, String email, String matricula) {
        return aRepository.searchAll(id, nome, email, matricula);
    }
}