package ifaluno.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Escola implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String nome;
	private Nivel nivel;

	public Escola() {
	}

	public Escola(Escola outro) {
		if (outro == null) {
			return;
		}
		this.id = outro.id;
		this.nome = outro.nome;
		this.nivel = outro.nivel;
	}
}
