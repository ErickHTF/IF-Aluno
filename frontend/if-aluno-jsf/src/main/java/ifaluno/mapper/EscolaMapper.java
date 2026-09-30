package ifaluno.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ifaluno.model.Escola;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class EscolaMapper {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static List<Escola> toList(InputStream inputStream) throws IOException {
		try {
			return OBJECT_MAPPER.readValue(inputStream, new TypeReference<>() {});
		}
		catch (JacksonException exc) {
			throw new IOException(exc);
		}
	}

	public static Escola toObject(InputStream inputStream) throws IOException {
		try {
			return OBJECT_MAPPER.readValue(inputStream, Escola.class);
		}
		catch (JacksonException exc) {
			throw new IOException(exc);
		}
	}

	public static Escola toObject(String json) throws IOException {
		try {
			return OBJECT_MAPPER.readValue(json, Escola.class);
		}
		catch (JacksonException exc) {
			throw new IOException(exc);
		}
	}

	public static String toJson(Escola e) throws IOException {
		try {
			return OBJECT_MAPPER.writeValueAsString(e);
		}
		catch (JacksonException exc) {
			throw new IOException(exc);
		}
	}
}
