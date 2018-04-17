package com.spillman.client.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ServletUtils {

	public static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	private ServletUtils() {
	}

	public static void sendBadRequest(HttpServletResponse response, String message) throws IOException {
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().print("{\"message\":\"" + message + "\"}");
	}

	public static void sendServerError(HttpServletResponse response, String message) throws IOException {
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().print("{\"message\":\"" + message + "\"}");
	}

}