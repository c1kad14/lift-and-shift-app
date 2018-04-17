package com.spillman.client.validator;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spillman.client.ApplicationConstants.FREQUENCY_PROPERTIES_KEY;
import static com.spillman.client.ApplicationConstants.LIFT_METHOD_DIR;
import static com.spillman.client.ApplicationConstants.LIFT_METHOD_DIR_PATH_PROPERTIES_KEY;
import static com.spillman.client.ApplicationConstants.LIFT_METHOD_DIR_PROPERTIES_KEY;
import static com.spillman.client.ApplicationConstants.LIFT_METHOD_FTP;
import static com.spillman.client.ApplicationConstants.LIFT_METHOD_PROPERTIES_KEY;
import static com.spillman.client.ApplicationConstants.WEBSOCKET_URL_PROPERTIES_KEY;

public class PropertiesValidator {

	private final Map<String, Object> properties;
	private final List<String> errors = new LinkedList<>();
	private static final List<String> FREQUENCIES = Arrays.asList("60", "1440");

	public PropertiesValidator(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void validate() {
		errors.clear();
		validateLiftOptions();
		validateFrequency();
		validateUrl();
	}

	public boolean valid() {
		return errors.isEmpty();
	}

	public String getMessage() {
		return errors.stream().collect(Collectors.joining(", "));
	}

	private void validateUrl() {
		String url = (String) properties.get(WEBSOCKET_URL_PROPERTIES_KEY);
		if (StringUtils.isBlank(url)) {
			errors.add("Please add websocket url");
		}
	}
	private void validateFrequency() {
		String frequency = (String) properties.get(FREQUENCY_PROPERTIES_KEY);
		if (StringUtils.isBlank(frequency)) {
			errors.add("Please add frequency");
		}
		if (FREQUENCIES.stream().filter(f -> f.equals(frequency)).count() == 0) {
			errors.add("Wrong frequency setting");
		}
	}

	private void validateLiftOptions() {
		String liftMethod = (String) properties.get(LIFT_METHOD_PROPERTIES_KEY);
		if (StringUtils.isBlank(liftMethod)) {
			errors.add("Please add lift method");
		}
		else {
			switch (liftMethod) {
				case LIFT_METHOD_DIR:
					validateDir();
					break;
				case LIFT_METHOD_FTP:
					throw new IllegalArgumentException("FTP not implemented");
//					break;
				default:
					throw new IllegalArgumentException("Unknown lift method: " + liftMethod);
			}
		}
	}

	private void validateDir() {
		Map<String, Object> dirLiftProperties = (Map<String, Object>) properties.get(LIFT_METHOD_DIR_PROPERTIES_KEY);
		if (dirLiftProperties == null) {
			errors.add("Error validating lift directory parameters");
			return;
		}
		String path = (String) dirLiftProperties.get(LIFT_METHOD_DIR_PATH_PROPERTIES_KEY);
		if (StringUtils.isBlank(path)) {
			errors.add("Please add directory path");
			return;
		}
		if (!Paths.get(path).toFile().exists()) {
			errors.add("Directory not exists");
		}
	}
}
