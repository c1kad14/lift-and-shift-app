package com.spillman.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TestResponse {

	private TestResponseStatus status;
	private String message;

}
