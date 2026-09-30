package ifaluno.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ifaluno.mapper.EscolaMapper;
import ifaluno.model.Escola;
import ifaluno.model.Nivel;
import ifaluno.utils.JsonUtils;
import lombok.extern.log4j.Log4j;

@Log4j
public class EscolaService implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Escola> buscarTodos() {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ESCOLA_API)).GET().build();

		return toList(send(request));
	}

	public Escola buscarPorId(Integer id) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ESCOLA_API + "/" + id)).GET().build();

		return toObject(send(request));
	}

	public List<Escola> pesquisar(Integer id, String nome, Nivel nivel) {
		List<String> params = new ArrayList<>();

		if (id != null) {
			params.add("id=" + id);
		}
		if (nome != null && !nome.isBlank()) {
			params.add("nome=" + encode(nome));
		}
		if (nivel != null) {
			params.add("nivel=" + nivel.name());
		}

		String url = JsonUtils.ESCOLA_FILTRO_API + (params.isEmpty() ? "" : "?" + String.join("&", params));

		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();

		return toList(send(request));
	}

	public void salvar(Escola escola) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ESCOLA_API))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(serialize(escola)))
				.build();

		sendString(request);
	}

	public void atualizar(Escola escola) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ESCOLA_API + "/" + escola.getId()))
				.header("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(serialize(escola)))
				.build();

		sendString(request);
	}

	public void excluir(Integer id) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ESCOLA_API + "/" + id)).DELETE().build();

		sendString(request);
	}

	private String serialize(Escola escola) {
		try {
			return EscolaMapper.toJson(escola);
		}
		catch (IOException e) {
			throw new RuntimeException("Falha ao serializar Escola.", e);
		}
	}

	private List<Escola> toList(InputStream inputStream) {
		try {
			return EscolaMapper.toList(inputStream);
		}
		catch (IOException e) {
			throw new RuntimeException("Falha ao ler a lista de Escolas.", e);
		}
	}

	private Escola toObject(InputStream inputStream) {
		try {
			return EscolaMapper.toObject(inputStream);
		}
		catch (IOException e) {
			throw new RuntimeException("Falha ao ler a Escola.", e);
		}
	}

	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	private String lerResposta(HttpResponse<InputStream> response) {
		try {
			return new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			return "";
		}
	}

	private InputStream send(HttpRequest request) {
		try {
			HttpResponse<InputStream> response = HttpClient.newHttpClient()
					.send(request, HttpResponse.BodyHandlers.ofInputStream());

			log.info("HTTP " + response.statusCode() + " " + request.method() + " " + request.uri());

			if (response.statusCode() >= 400) {
				throw ApiException.fromHttp(response.statusCode(), lerResposta(response), request.uri());
			}

			return response.body();
		}
		catch (IOException e) {
			throw new RuntimeException("Falha de comunicação com a API IF-Aluno: " + e.getMessage(), e);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Requisição interrompida.", e);
		}
	}

	private String sendString(HttpRequest request) {
		try {
			HttpResponse<String> response = HttpClient.newHttpClient()
					.send(request, HttpResponse.BodyHandlers.ofString());

			log.info("HTTP " + response.statusCode() + " " + request.method() + " " + request.uri());

			if (response.statusCode() >= 400) {
				throw ApiException.fromHttp(response.statusCode(), response.body(), request.uri());
			}

			return response.body();
		}
		catch (IOException e) {
			throw new RuntimeException("Falha de comunicação com a API IF-Aluno: " + e.getMessage(), e);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Requisição interrompida.", e);
		}
	}
}
