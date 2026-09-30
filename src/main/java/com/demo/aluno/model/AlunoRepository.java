package com.demo.aluno.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {

    @Query("select a from Aluno a where " +
           "a.id = coalesce(:id, a.id) and " +
           "lower(a.nome) like lower(concat('%', coalesce(:nome, a.nome), '%')) and " +
           "lower(a.email) like lower(concat('%', coalesce(:email, a.email), '%')) and " +
           "lower(a.matricula) like lower(concat('%', coalesce(:matricula, a.matricula), '%'))")
    List<Aluno> searchMainParameters(@Param("id") Long id,
                          @Param("nome") String nome,
                          @Param("email") String email,
                          @Param("matricula") String matricula);
}
