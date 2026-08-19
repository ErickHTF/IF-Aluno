package com.demo.aluno.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer> {

    @Query("select a from Aluno a where " +
           "(:id is null or a.id = :id) and " +
           "(:nome is null or a.nome like %:nome%) and " +
           "(:email is null or a.email like %:email%) and " +
           "(:matricula is null or a.matricula like %:matricula%)")
    List<Aluno> searchAll(@Param("id") Integer id,
                          @Param("nome") String nome,
                          @Param("email") String email,
                          @Param("matricula") String matricula);
}