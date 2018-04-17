package com.spillman.client.ship;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
public class ShipEndpoint extends Endpoint {

	private Session session;

	@Override public void onOpen(Session session, EndpointConfig endpointConfig) {
		this.session = session;
	}

	public void disconnect() throws IOException {
		if (session != null) {
			log.debug("Closing connection");
			session.close();
			session = null;
		}
	}

	public void testConnection() throws IOException {
		session.getBasicRemote().sendText("test");
	}

	public void sendMessage(String message) throws IOException {
		session.getBasicRemote().sendText(message);
	}

}
