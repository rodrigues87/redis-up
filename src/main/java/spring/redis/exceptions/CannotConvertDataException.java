package spring.redis.exceptions;

import java.io.Serial;

public class CannotConvertDataException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = -7114487653197757339L;
	
	public CannotConvertDataException(String className) {
		super(String.format("The object retrieved from database isn't the type : %s", className));
	}
}
