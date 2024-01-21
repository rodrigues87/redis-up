package redis.up.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import redis.up.constants.ErrorCodes;
import redis.up.exceptions.CannotConvertDataException;
import redis.up.exceptions.CannotReadValueAsStringException;
import redis.up.exceptions.ExceptionResponse;


@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String INVALID_ARGUMENTS = "Invalid Arguments";
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		logger.error("An unexpected error occur", ex);
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				ErrorCodes.INTERNAL_SERVER_ERROR, ex.getMessage());
		
		request.getDescription(false);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		logger.error(INVALID_ARGUMENTS, ex);
		String error = ex.getName() + " should be of type " + ex.getMostSpecificCause();
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.INVALID_REQUEST, error);
		return ResponseEntity.badRequest().body(exceptionResponse);
	}
	
	@Override
	public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("RestExceptionHandler.handleExceptionInternal - Generic error - error: [{}]", ex.getMessage(), ex);
		ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.INVALID_REQUEST, ex.getMessage());
		return super.handleExceptionInternal(ex, exceptionResponse, headers, status, request);
	}
	
	
	@ExceptionHandler(CannotConvertDataException.class)
	public ResponseEntity<Object> handleBadRequestException(CannotConvertDataException ex) {
		log.info("onCannotConvertDataException: {} ", ex.getClass(), ex.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.BAD_REQUEST, ex.getMessage());
		log.info("RestExceptionHandler.handleBadRequestException - class: {}, message: {}", ex.getClass(), ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(CannotReadValueAsStringException.class)
	public ResponseEntity<Object> handleNotFoundException(CannotReadValueAsStringException ex) {
		log.info("onHandleNotFoundException: {} ", ex.getClass(), ex.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(ErrorCodes.BAD_REQUEST, ex.getMessage());
		log.info("RestExceptionHandler.handleNotFoundException - class: {}, message: {}", ex.getClass(), ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
	}
}