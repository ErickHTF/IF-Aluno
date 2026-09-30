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

import ifaluno.mapper.AlunoMapper;
import ifaluno.model.Aluno;
import ifaluno.utils.JsonUtils;
import lombok.extern.log4j.Log4j;

@Log4j
public class AlunoService implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Aluno> buscarTodos() {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ALUNO_API)).GET().build();

		return toList(send(request));
	}

	public Aluno buscarPorId(Long id) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ALUNO_API + "/" + id)).GET().build();

		return toObject(send(request));
	}

	public List<Aluno> pesquisar(Long id, String nome, String email, String matricula) {
		List<String> params = new ArrayList<>();

		if (id != null) {
			params.add("id=" + id);
		}
		if (nome != null && !nome.isBlank()) {
			params.add("nome=" + encode(nome));
		}
		if (email != null && !email.isBlank()) {
			params.add("email=" + encode(email));
		}
		if (matricula != null && !matricula.isBlank()) {
			params.add("matricula=" + encode(matricula));
		}

		String url = JsonUtils.ALUNO_FILTRO_API + (params.isEmpty() ? "" : "?" + String.join("&", params));

		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();

		return toList(send(request));
	}

	public void salvar(Aluno aluno) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ALUNO_API))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(serialize(aluno)))
				.build();

		sendString(request);
	}

	public void atualizar(Aluno aluno) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ALUNO_API + "/" + aluno.getId()))
				.header("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(serialize(aluno)))
				.build();

		sendString(request);
	}

	public void excluir(Long id) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(JsonUtils.ALUNO_API + "/" + id)).DELETE().build();

		sendString(request);
	}

	private String serialize(Aluno aluno) {
		try {
			return AlunoMapper.toJson(aluno);
		}
		catch (IOException e) {
			throw new RuntimeException("Falha ao serializar Aluno.", e);
		}
	}

	private List<Aluno> toList(InputStream inputStream) {
		try {
			return AlunoMapper.toList(inputStream);
		}
		catch (IOException e) {
			throw new RuntimeException("Falha ao ler a lista de Alunos.", e);
		}
	}

	private Aluno toObject(InputStream inputStream) {
		try {
			return AlunoMapper.toObject(inputStream);
		}
		catch (IOException e) {
			throw new RuntimeException("Falha ao ler o Aluno.", e);
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
