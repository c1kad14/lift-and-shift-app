package com.spillman.client.servlet;

import com.spillman.client.ApplicationStarter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shutdown")
@Slf4j
public class ShutdownServlet extends HttpServlet {

	@Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.debug("Shutting down app");
		ApplicationStarter.shutdown();
	}
}
