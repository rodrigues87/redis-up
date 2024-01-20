package spring.redis.exceptions.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import spring.redis.exceptions.CannotConvertDataException;
import spring.redis.exceptions.CannotReadValueAsStringException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RestExceptionHandlerTest {
	
	private RestExceptionHandler restExceptionHandler;
	
	@BeforeEach
	void setUp() {
		restExceptionHandler = new RestExceptionHandler();
	}
	
	@Test
	void handleAllExceptions_ShouldReturnInternalServerError() {
		Exception ex = new Exception("Test exception");
		WebRequest request = mock(WebRequest.class);
		
		ResponseEntity<Object> responseEntity = restExceptionHandler.handleAllExceptions(ex, request);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
	
	@Test
	void handleMethodArgumentTypeMismatchException_ShouldReturnBadRequest() {
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException("param", String.class, null, null, null);
		
		ResponseEntity<Object> responseEntity = restExceptionHandler.handleMethodArgumentTypeMismatchException(ex);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	
	@Test
	void handleExceptionInternal_ShouldReturnInvalidRequest() {
		Exception ex = new Exception("Test exception");
		WebRequest request = mock(WebRequest.class);
		
		ResponseEntity<Object> responseEntity = restExceptionHandler.handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}
	
	@Test
	void handleBadRequestException_ShouldReturnBadRequest() {
		CannotConvertDataException ex = new CannotConvertDataException("Test exception");
		
		ResponseEntity<Object> responseEntity = restExceptionHandler.handleBadRequestException(ex);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	void handleNotFoundException_ShouldReturnNotFound() {
		CannotReadValueAsStringException ex = new CannotReadValueAsStringException("Test exception");
		
		ResponseEntity<Object> responseEntity = restExceptionHandler.handleNotFoundException(ex);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}