package com.redis.up.constants;

import lombok.Getter;

@Getter
public enum ErrorCodes {
	INTERNAL_SERVER_ERROR("Internal server error"),
	INVALID_REQUEST("Invalid request"),
	BAD_REQUEST("Bad request"),
	VALIDATION_FAILED("Validation failed"),
	NOT_FOUND("Not found");

	private final String message;

    ErrorCodes(String message) {
    	this.message = message;
    }
	
}
