package com.demo.aluno.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {

    @Query("select a from Aluno a where " +
           "(:id is null or a.id = :id) and " +
           "(:nome is null or lower(a.nome) like lower(concat('%', :nome, '%'))) and " +
           "(:email is null or lower(a.email) like lower(concat('%', :email, '%'))) and " +
           "(:matricula is null or lower(a.matricula) like lower(concat('%', :matricula, '%')))")
    List<Aluno> searchMainParameters(@Param("id") Long id,
                          @Param("nome") String nome,
                          @Param("email") String email,
                          @Param("matricula") String matricula);
}
