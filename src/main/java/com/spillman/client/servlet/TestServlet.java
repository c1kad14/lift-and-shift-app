package com.spillman.client.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spillman.client.model.TestResponse;
import com.spillman.client.service.TestService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Inject
	private TestService testService;

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TestResponse testResponse = testService.test(request);
		response.getWriter().print(mapper.writeValueAsString(testResponse));
	}
}
