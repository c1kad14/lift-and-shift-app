package com.spillman.client.endpoint;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/status")
@Slf4j
public class StatusEndpoint {

	private static final List<Session> subscribers = new ArrayList<>();
	private static boolean running = false;

	@OnOpen
	public void open(Session session) {
		log.debug("Opened status update channel for: {}", session.getId());
		subscribers.add(session);
		sendMessage(session, Boolean.toString(running));
	}

	@OnClose
	public void close(Session session) {
		log.debug("Closed status update channel for: {}", session.getId());
		subscribers.remove(session);
		try {
			session.close();
		}
		catch (IOException ex) {
			log.error("Error closing status update channel for: {}, {}", session.getId(), ex.getMessage());
		}
	}

	public static void updateStatus(boolean running) {
		StatusEndpoint.running = running;
		subscribers.forEach(session -> sendMessage(session, Boolean.toString(running)));
	}

	private static void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		}
		catch (IOException ex) {
			log.error("Error sending status update to: {}", session.getId());
		}
	}

}