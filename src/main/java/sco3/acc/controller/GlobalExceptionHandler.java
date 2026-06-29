package sco3.acc.controller;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handle(BadRequestException ex) {
		return Map.of("error", ex.getMessage());
	}
}