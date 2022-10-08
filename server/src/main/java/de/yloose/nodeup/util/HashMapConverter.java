package de.yloose.nodeup.util;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, Object> data) {

		String dataJson = null;
		try {
			dataJson = objectMapper.writeValueAsString(data);
		} catch (final JsonProcessingException e) {
			// TODO: Log exception
		}

		return dataJson;
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dataJson) {

		Map<String, Object> data = null;
		try {
			data = objectMapper.readValue(dataJson, Map.class);
		} catch (final IOException e) {
			// TODO: Log exception
		}

		return data;
	}
}