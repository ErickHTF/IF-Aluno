package com.demo.aluno.service;

import com.demo.aluno.dto.AlunoDTO;
import com.demo.aluno.model.Aluno;
import com.demo.aluno.model.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository aRepository;

    public Aluno saveAluno(AlunoDTO alunoDTO) {
        Aluno a = new Aluno();

        copyToEntity(alunoDTO, a);

        return aRepository.save(a);
    }

    public Optional<Aluno> updateAluno(Long id, AlunoDTO alunoDTO) {
        Optional<Aluno> alunoData = aRepository.findById(id);

        if (alunoData.isPresent()) {
            Aluno a = alunoData.get();

            copyToEntity(alunoDTO, a);

            return Optional.of(aRepository.save(a));
        }

        return Optional.empty();
    }

    private void copyToEntity(AlunoDTO alunoDTO, Aluno a) {
        a.setEmail(alunoDTO.getEmail());
        a.setMatricula(alunoDTO.getMatricula());
        a.setNome(alunoDTO.getNome());
        a.setCpf(alunoDTO.getCpf());
        a.setAltura(alunoDTO.getAltura());
        a.setTelefone(alunoDTO.getTelefone());
        a.setDtNasc(alunoDTO.getDtNasc());
        a.setAtivo(alunoDTO.getAtivo());
    }

    public Optional<Aluno> findById(Long id) {
        return aRepository.findById(id);
    }

    public void deleteById(Long id) {
        aRepository.deleteById(id);
    }

    public List<Aluno> getAll() {
        return (List<Aluno>) aRepository.findAll();
    }

    public List<Aluno> searchAluno(Long id, String nome, String email, String matricula) {
        return aRepository.searchMainParameters(id, nome, email, matricula);
    }
}
