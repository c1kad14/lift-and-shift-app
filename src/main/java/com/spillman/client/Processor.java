package com.spillman.client;

import com.spillman.client.endpoint.StatusEndpoint;
import com.spillman.client.model.PropertiesHolder;
import com.spillman.client.ship.ShipEndpoint;
import com.spillman.client.ship.ShipEndpointFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class Processor {

	private static final String ACCESS_MODE = "r";
	private static final long DELAY_FOR_TIMER = 60L + 1000;
	private LocalDateTime lastRunTime;
	private Timer timer;
	private int minutesFrequency;

	public void startProcessing() {
		lastRunTime = LocalDateTime.now();
		String frequency = PropertiesHolder.getPropertyValue(ApplicationConstants.FREQUENCY_PROPERTIES_KEY);
		minutesFrequency = Integer.parseInt(frequency);
		timer = new Timer();
		timer.schedule(new ProcessTask(), DELAY_FOR_TIMER);
		log.debug("Processing timer started");
		StatusEndpoint.updateStatus(true);
	}

	public void stopProcessing() {
		if (timer != null) {
			timer.cancel();
			log.debug("Processing timer stopped");
		}
		StatusEndpoint.updateStatus(false);
	}

	private class ProcessTask extends TimerTask {

		public void run() {
			if (checkTime()) {
				try {
					sendFilesFromDirectory();
				}
				catch (ProcessingException ex) {
					log.error("Error processing files", ex);
				}
			}
		}

		private boolean checkTime() {
			return LocalDateTime.now().minusMinutes(minutesFrequency).isAfter(lastRunTime);
		}
	}

	public static void sendFilesFromDirectory() throws ProcessingException {
		String path = (String) PropertiesHolder
			.getNestedProperty(ApplicationConstants.LIFT_METHOD_DIR_PROPERTIES_KEY)
			.get(ApplicationConstants.LIFT_METHOD_DIR_PATH_PROPERTIES_KEY);
		File[] files = Paths.get(path).toFile().listFiles();
		if (files == null) {
			log.error("Error processing files from: {}", path);
		}
		else if (files.length == 0) {
			log.info("Nothing to process");
		}
		else {
			int errors = 0;
			for (File file : files) {
				try {
					sendFile(file.getAbsolutePath());
					Files.delete(Paths.get(file.getAbsolutePath()));
				}
				catch (ProcessingException ex) {
					log.error("Error sending file: {}", file.getAbsolutePath(), ex);
					errors++;
				}
				catch (IOException ex) {
					log.error("Error deleting file: {}", file.getAbsolutePath(), ex);
				}
			}
			if (errors > 0) {
				throw new ProcessingException("Error processing one or more files");
			}
		}
	}

	private static void sendFile(String filename) throws ProcessingException {
		String contents;
		try {
			contents = new String(Files.readAllBytes(Paths.get(filename)));
		}
		catch (IOException ex) {
			log.error("Error reading file: {}", filename);
			throw new ProcessingException("Error reading file: " + filename);
		}
		ShipEndpointFactory factory = new ShipEndpointFactory();
		String url = PropertiesHolder.getPropertyValue(ApplicationConstants.WEBSOCKET_URL_PROPERTIES_KEY);
		try {
			ShipEndpoint endpoint = factory.buildEndpoint(url);
			endpoint.sendMessage(contents);
			endpoint.disconnect();
		}
		catch (Exception ex) {
			log.error("Error sending file to websocket", ex);
			throw new ProcessingException("Error sending file " + filename + " to websocket");
		}
	}

}
