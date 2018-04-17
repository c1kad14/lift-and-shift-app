package com.spillman.client.servlet;

import com.spillman.client.Processor;
import com.spillman.client.model.PropertiesHolder;
import com.spillman.client.validator.PropertiesValidator;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/process")
@Slf4j
public class ProcessServlet extends HttpServlet {

	@Inject
	private Processor processor;

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> properties = PropertiesHolder.getProperties();
		PropertiesValidator validator = new PropertiesValidator(properties);
		validator.validate();
		if (!validator.valid()) {
			ServletUtils.sendBadRequest(response, "Invalid properties: " + validator.getMessage() + "\nProcessing not started");
			log.debug("Invalid properties, processing not started");
			return;
		}
		processor.startProcessing();
	}

	@Override protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processor.stopProcessing();
	}
}
