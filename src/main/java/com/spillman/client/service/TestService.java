package com.spillman.client.service;

import com.spillman.client.ProcessingException;
import com.spillman.client.Processor;
import com.spillman.client.model.TestResponse;
import com.spillman.client.model.TestResponseStatus;
import com.spillman.client.servlet.ProcessServlet;
import com.spillman.client.ship.ShipEndpoint;
import com.spillman.client.ship.ShipEndpointFactory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Slf4j
public class TestService {

	private static final TestResponse unknownResponse = new TestResponse(TestResponseStatus.ERROR, "Unknown File Pickup Type");

	public TestResponse test(HttpServletRequest request) {
		String pickupType = request.getParameter("value");
		TestResponse result;
		switch (pickupType) {
			case "dir":
				result = testDirectory(request);
				break;
			case "ftp":
				throw new IllegalArgumentException("FTP test not implemented");
//				break;
			case "ws":
				result = testWebsocket(request);
				break;
			case "ship":
				result = testShip();
				break;
			default:
				log.debug("Unknown pickup type: {}", pickupType);
				result = unknownResponse;
		}
		return result;
	}

	private TestResponse testShip() {
		try {
			Processor.sendFilesFromDirectory();
		}
		catch (ProcessingException ex) {
			return new TestResponse(TestResponseStatus.ERROR, ex.getMessage());
		}
		return new TestResponse(TestResponseStatus.OK, "Files exported successfully");
	}

	private TestResponse testDirectory(HttpServletRequest request) {
		String path = request.getParameter("path");
		File directory = new File(path);
		TestResponseStatus status;
		String message;
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				status = TestResponseStatus.INFO;
				message = "Directories created";
			}
			else {
				status = TestResponseStatus.ERROR;
				message = "Error creating directories";
			}
		}
		else {
			status = TestResponseStatus.OK;
			message = "Directory exists";
		}
		return new TestResponse(status, message);
	}

	private TestResponse testWebsocket(HttpServletRequest request) {
		String url = request.getParameter("url");
		ShipEndpointFactory factory = new ShipEndpointFactory();
		ShipEndpoint endpoint = null;
		try {
			endpoint = factory.buildEndpoint(url);
		}
		catch (Exception ex) {
			log.error("Error testing websocket connection: {}", ex.getMessage());
			return new TestResponse(TestResponseStatus.ERROR, "Error connecting to server");
		}
		try {
			endpoint.testConnection();
		}
		catch (IOException ex) {
			log.error("Error sending websocket test message: {}", ex.getMessage());
			return new TestResponse(TestResponseStatus.ERROR, "Error sending test message");
		}
		try {
			endpoint.disconnect();
		}
		catch (IOException ex) {
			log.error("Error disconnecting websocket: {}", ex.getMessage());
		}
		return new TestResponse(TestResponseStatus.OK, "Connection tested");
	}

}
