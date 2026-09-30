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

import ifaluno.model.Escola;
import ifaluno.model.Nivel;
import ifaluno.service.EscolaService;
import ifaluno.service.ApiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class ManterEscolaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Escola> escolas = new ArrayList<>();
	private Escola escola = new Escola();

	private Integer filtroId;
	private String filtroNome;
	private Nivel filtroNivel;

	@Inject
	private EscolaService escolaService;

	@PostConstruct
	public void inicializar() {
		log.info("ManterEscola inicializar()");
		carregarEscolas();
	}

	public Nivel[] getNiveis() {
		return Nivel.values();
	}

	public void novo() {
		limpar();
	}

	public void editar(Escola escola) {
		this.escola = new Escola(escola);
	}

	public void salvar() {
		log.info("salvando Escola...");

		try {
			if (escola.getId() == null) {
				escolaService.salvar(escola);
				addInfo("Escola cadastrada com sucesso!");
			}
			else {
				escolaService.atualizar(escola);
				addInfo("Escola alterada com sucesso!");
			}

			limpar();
			carregarEscolas();
		}
		catch (RuntimeException e) {
			log.error("Erro ao salvar Escola", e);
			addError("Erro ao salvar escola: " + mensagemErro(e));
			PrimeFaces.current().ajax().addCallbackParam("erroApi", true);
		}
	}

	public void excluir() {
		log.info("excluindo Escola...");

		try {
			if (escola != null && escola.getId() != null) {
				escolaService.excluir(escola.getId());
				addInfo("Escola excluída com sucesso!");
			}

			limpar();
			carregarEscolas();
		}
		catch (RuntimeException e) {
			log.error("Erro ao excluir Escola", e);
			addError("Erro ao excluir escola: " + mensagemErro(e));
		}
	}

	public void pesquisar() {
		log.info("pesquisando Escola...");

		try {
			this.escolas = escolaService.pesquisar(filtroId, filtroNome, filtroNivel);
		}
		catch (RuntimeException e) {
			log.error("Erro ao pesquisar Escola", e);
			addError("Erro ao pesquisar escola: " + mensagemErro(e));
		}
	}

	public void limparFiltros() {
		this.filtroId = null;
		this.filtroNome = null;
		this.filtroNivel = null;

		carregarEscolas();
	}

	public void limpar() {
		log.info("limpar");
		this.escola = new Escola();
	}

	private void carregarEscolas() {
		try {
			this.escolas = escolaService.buscarTodos();
		}
		catch (RuntimeException e) {
			log.error("Erro ao carregar Escolas", e);
			addError("Não foi possível carregar as escolas: " + mensagemErro(e));
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
