package com.redis.up.exceptions;

import java.io.Serial;

public class CannotReadValueAsStringException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = -7114487653197757339L;
	
	public CannotReadValueAsStringException(Object object) {
		super(String.format("The object can't be converted to string: %s", object));
	}
}
