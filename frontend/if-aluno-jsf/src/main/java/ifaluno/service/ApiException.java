package ifaluno.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final int status;
	private final List<String> mensagens;
	private final Map<String, String> campos;

	public ApiException(int status, String mensagem, List<String> mensagens, Map<String, String> campos) {
		super(mensagem);
		this.status = status;
		this.mensagens = mensagens;
		this.campos = campos;
	}

	public int getStatus() {
		return status;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public Map<String, String> getCampos() {
		return campos;
	}

	public static ApiException fromHttp(int status, String body, URI uri) {
		List<String> mensagens = new ArrayList<>();
		Map<String, String> campos = new LinkedHashMap<>();

		if (body != null && !body.isBlank()) {
			try {
				Map<String, Object> json = MAPPER.readValue(body, new TypeReference<Map<String, Object>>() {});
				extrairLista(json.get("mensagens"), mensagens);
				extrairCampos(json.get("campos"), campos);
				extrairTexto(json.get("mensagem"), mensagens);
				extrairTexto(json.get("erro"), mensagens);
				extrairTexto(json.get("message"), mensagens);
			}
			catch (Exception ignored) {
				// corpo não é JSON: usa a mensagem padrão abaixo
			}
		}

		if (mensagens.isEmpty()) {
			mensagens.add(mensagemPadrao(status));
		}

		String local = uri == null ? "" : " em " + uri;
		String mensagem = "Erro HTTP " + status + local + ": " + String.join(" ", mensagens);

		return new ApiException(status, mensagem, mensagens, campos);
	}

	private static void extrairLista(Object valor, List<String> destino) {
		if (valor instanceof List<?> lista) {
			for (Object item : lista) {
				if (item != null && !String.valueOf(item).isBlank()) {
					destino.add(String.valueOf(item));
				}
			}
		}
	}

	private static void extrairCampos(Object valor, Map<String, String> destino) {
		if (valor instanceof Map<?, ?> mapa) {
			for (Map.Entry<?, ?> entry : mapa.entrySet()) {
				destino.put(String.valueOf(entry.getKey()),
						entry.getValue() == null ? null : String.valueOf(entry.getValue()));
			}
		}
	}

	private static void extrairTexto(Object valor, List<String> destino) {
		if (valor != null && destino.isEmpty() && !String.valueOf(valor).isBlank()) {
			destino.add(String.valueOf(valor));
		}
	}

	private static String mensagemPadrao(int status) {
		if (status == 400) {
			return "Dados inválidos enviados para a API.";
		}
		if (status == 404) {
			return "Registro não encontrado.";
		}
		if (status == 409) {
			return "Registro já cadastrado.";
		}
		if (status >= 500) {
			return "Erro interno na API. Tente novamente mais tarde.";
		}
		return "Não foi possível concluir a operação.";
	}
}
