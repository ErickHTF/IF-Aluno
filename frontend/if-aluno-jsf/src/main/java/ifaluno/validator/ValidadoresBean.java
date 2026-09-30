package ifaluno.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

@Named("validadores")
@ApplicationScoped
public class ValidadoresBean {

	private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

	public void nome(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return;
		}

		String texto = value.toString().trim();

		if (texto.length() < 3 || texto.length() > 100) {
			throw new ValidatorException(erro("O nome deve ter entre 3 e 100 caracteres."));
		}
	}

	public void email(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return;
		}

		String texto = value.toString().trim();

		if (!EMAIL.matcher(texto).matches()) {
			throw new ValidatorException(erro("Informe um email válido (ex.: email@aluno.ifsp.edu.br)."));
		}
	}

	public void dataPassada(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return;
		}

		LocalDate data;
		if (value instanceof LocalDate localDate) {
			data = localDate;
		}
		else if (value instanceof Date date) {
			data = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		else {
			return;
		}

		if (!data.isBefore(LocalDate.now())) {
			throw new ValidatorException(erro("A data de nascimento deve ser uma data passada."));
		}
	}

	private FacesMessage erro(String mensagem) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
	}
}
