package com.demo.escola.service;

import com.demo.escola.dto.EscolaDTO;
import com.demo.escola.model.Escola;
import com.demo.escola.model.EscolaRepository;
import com.demo.escola.model.Nivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EscolaService {

    @Autowired
    private EscolaRepository eRepository;

    public Escola saveEscola(EscolaDTO escolaDTO) {
        Escola e = new Escola();

        copyToEntity(escolaDTO, e);

        return eRepository.save(e);
    }

    public Optional<Escola> updateEscola(Integer id, EscolaDTO escolaDTO) {
        Optional<Escola> escolaData = eRepository.findById(id);

        if (escolaData.isPresent()) {
            Escola e = escolaData.get();

            copyToEntity(escolaDTO, e);

            return Optional.of(eRepository.save(e));
        }

        return Optional.empty();
    }

    private void copyToEntity(EscolaDTO escolaDTO, Escola e) {
        e.setNome(escolaDTO.getNome());
        e.setNivel(escolaDTO.getNivel());
    }

    public Optional<Escola> findById(Integer id) {
        return eRepository.findById(id);
    }

    public void deleteById(Integer id) {
        eRepository.deleteById(id);
    }

    public List<Escola> getAll() {
        return (List<Escola>) eRepository.findAll();
    }

    public List<Escola> searchEscola(Integer id, String nome, Nivel nivel) {
        return eRepository.searchAll(id, nome, nivel);
    }
}
