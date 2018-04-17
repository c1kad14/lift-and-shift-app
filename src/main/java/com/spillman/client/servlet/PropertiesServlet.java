package com.spillman.client.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spillman.client.model.PropertiesHolder;
import com.spillman.client.validator.PropertiesValidator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static com.spillman.client.servlet.ServletUtils.CONTENT_TYPE;

@WebServlet("/properties")
@Slf4j
public class PropertiesServlet extends HttpServlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> properties = PropertiesHolder.getProperties();
		response.setContentType(CONTENT_TYPE);
		response.getWriter().print(mapper.writeValueAsString(properties));
	}

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String body = getBody(request);
			Map<String, Object> properties = mapper.readValue(body, new TypeReference<Map<String, Object>>(){});
			PropertiesValidator validator = new PropertiesValidator(properties);
			validator.validate();
			if (!validator.valid()) {
				ServletUtils.sendBadRequest(response, "Invalid properties: " + validator.getMessage());
				log.debug("Invalid properties: {}", body);
				return;
			}
			log.debug("Updating properties: {}", body);
			PropertiesHolder.setProperties(properties);
		} catch (Exception ex) {
			log.error("Error updating properties", ex);
			ServletUtils.sendServerError(response, "Error updating properties");
		}
	}

	private static String getBody(HttpServletRequest request) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		try (InputStream inputStream = request.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
		} catch (IOException ex) {
			log.error("Error reading request body");
			throw ex;
		}
		return stringBuilder.toString();
	}
}
