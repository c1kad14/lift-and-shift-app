package com.spillman.client.ship;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ShipEndpointFactory {

	public ShipEndpoint buildEndpoint(String url) throws URISyntaxException, IOException, DeploymentException {
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		ShipEndpoint endpoint = new ShipEndpoint();
		container.connectToServer(endpoint, config, new URI(url));
		return endpoint;
	}

}