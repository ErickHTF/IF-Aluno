package ifaluno.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aluno implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String nome;
	private String email;
	private String matricula;
	private String cpf;
	private Float altura;
	private String telefone;
	private LocalDate dtNasc;
	private Boolean ativo;

	public Aluno() {
	}

	public Aluno(Aluno outro) {
		if (outro == null) {
			return;
		}
		this.id = outro.id;
		this.nome = outro.nome;
		this.email = outro.email;
		this.matricula = outro.matricula;
		this.cpf = outro.cpf;
		this.altura = outro.altura;
		this.telefone = outro.telefone;
		this.dtNasc = outro.dtNasc;
		this.ativo = outro.ativo;
	}
}
