package redis.up.exceptions;

import org.junit.jupiter.api.Test;
import redis.up.constants.ErrorCodes;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionResponseTest {
	
	@Test
	void constructor_ShouldSetFieldsCorrectly() {
		// Arrange
		ErrorCodes errorCode = ErrorCodes.INTERNAL_SERVER_ERROR;
		String details = "Test details";
		
		// Act
		ExceptionResponse exceptionResponse = new ExceptionResponse(errorCode, details);
		
		// Assert
		assertEquals(errorCode.name(), exceptionResponse.getCode());
		assertEquals(errorCode.getMessage(), exceptionResponse.getMessage());
		assertEquals(Collections.singletonList(details), exceptionResponse.getDetails());
	}
}