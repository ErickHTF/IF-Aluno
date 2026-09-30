package com.demo.escola.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscolaRepository extends CrudRepository<Escola, Integer> {

    @Query("select e from Escola e where " +
           "(:id is null or e.id = :id) and " +
           "(:nome is null or lower(e.nome) like lower(concat('%', :nome, '%'))) and " +
           "(:nivel is null or e.nivel = :nivel)")
    List<Escola> searchAll(@Param("id") Integer id,
                           @Param("nome") String nome,
                           @Param("nivel") Nivel nivel);
}
