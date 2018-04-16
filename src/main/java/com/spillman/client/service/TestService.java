package com.spillman.client.service;

import com.spillman.client.model.TestResponse;
import com.spillman.client.model.TestResponseStatus;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

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
			default:
				log.debug("Unknown pickup type: {}", pickupType);
				result = unknownResponse;
		}
		return result;
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

}
