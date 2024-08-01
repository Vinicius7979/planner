package com.rocketseat.planner.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class TripsExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> genericException(Exception ex) {

		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now())
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).status(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.errors(List.of(ex.getMessage())).build();

		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> camposInvalidosException(DataIntegrityViolationException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now())
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).status(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.errors(List.of(ex.getMessage())).build();
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ApiError> formatoDataException(DateTimeParseException ex) {
		ApiError apiError = ApiError.builder().timestamp(LocalDateTime.now())
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).status(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.errors(List.of(ex.getMessage())).build();
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
