package ua.skillsUp.wharehouse.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CanNotPerformOperationException extends RuntimeException {
	public CanNotPerformOperationException(String message) {
		super(message);
	}
}
