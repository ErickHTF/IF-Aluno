package ifaluno.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import ifaluno.model.Aluno;
import ifaluno.service.AlunoService;
import ifaluno.service.ApiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class ManterAlunoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Aluno> alunos = new ArrayList<>();
	private Aluno aluno = new Aluno();

	private Long filtroId;
	private String filtroNome;
	private String filtroEmail;
	private String filtroMatricula;

	@Inject
	private AlunoService alunoService;

	@PostConstruct
	public void inicializar() {
		log.info("ManterAluno inicializar()");
		carregarAlunos();
	}

	public void novo() {
		limpar();
	}

	public void editar(Aluno aluno) {
		this.aluno = new Aluno(aluno);
	}

	public void salvar() {
		log.info("salvando Aluno...");

		try {
			if (aluno.getId() == null) {
				alunoService.salvar(aluno);
				addInfo("Aluno cadastrado com sucesso!");
			}
			else {
				alunoService.atualizar(aluno);
				addInfo("Aluno alterado com sucesso!");
			}

			limpar();
			carregarAlunos();
		}
		catch (RuntimeException e) {
			log.error("Erro ao salvar Aluno", e);
			addError("Erro ao salvar aluno: " + mensagemErro(e));
			PrimeFaces.current().ajax().addCallbackParam("erroApi", true);
		}
	}

	public void excluir() {
		log.info("excluindo Aluno...");

		try {
			if (aluno != null && aluno.getId() != null) {
				alunoService.excluir(aluno.getId());
				addInfo("Aluno excluído com sucesso!");
			}

			limpar();
			carregarAlunos();
		}
		catch (RuntimeException e) {
			log.error("Erro ao excluir Aluno", e);
			addError("Erro ao excluir aluno: " + mensagemErro(e));
		}
	}

	public void pesquisar() {
		log.info("pesquisando Aluno...");

		try {
			this.alunos = alunoService.pesquisar(filtroId, filtroNome, filtroEmail, filtroMatricula);
		}
		catch (RuntimeException e) {
			log.error("Erro ao pesquisar Aluno", e);
			addError("Erro ao pesquisar aluno: " + mensagemErro(e));
		}
	}

	public void limparFiltros() {
		this.filtroId = null;
		this.filtroNome = null;
		this.filtroEmail = null;
		this.filtroMatricula = null;

		carregarAlunos();
	}

	public void limpar() {
		log.info("limpar");
		this.aluno = new Aluno();
		this.aluno.setAtivo(Boolean.TRUE);
	}

	private void carregarAlunos() {
		try {
			this.alunos = alunoService.buscarTodos();
		}
		catch (RuntimeException e) {
			log.error("Erro ao carregar Alunos", e);
			addError("Não foi possível carregar os alunos: " + mensagemErro(e));
		}
	}

	private String mensagemErro(RuntimeException e) {
		if (e instanceof ApiException api) {
			return String.join(" ", api.getMensagens());
		}
		return e.getMessage();
	}

	private void addInfo(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
	}

	private void addError(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
	}
}
