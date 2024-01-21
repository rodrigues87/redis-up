package redis.up.exceptions;

import lombok.Getter;
import redis.up.constants.ErrorCodes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;
	private final String message;
	private final List<String> details;

    public ExceptionResponse(final ErrorCodes errorCode, String details) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.details = Collections.singletonList(details);
    }

}
