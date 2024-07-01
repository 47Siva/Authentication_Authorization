package com.app.Authentication.Authorization.advice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.app.Authentication.Authorization.response.Error;
import com.app.Authentication.Authorization.response.Response;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	protected ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		Response response = new Response();
		response.setData("Validation Failed");
		Error err = new Error();
		err.setCode(HttpStatus.BAD_REQUEST.toString());
		err.setReason("Validation Failed");
		response.setError(err);
		List<String> errors = new ArrayList<>();
		BindingResult bindingResult = ex.getBindingResult();
		bindingResult.getAllErrors().forEach(error -> errors.add(error.getCode()));
		response.setErrorMessages(errors);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ObjectInvalidException.class)
	public ResponseEntity<?> handleInvalidObjectException(Exception ex, WebRequest request) {
		Error errors = new Error();
		errors.setErrorList((Stream.of(ex.getMessage().split(",")).collect(Collectors.toList())));
		errors.setReason(ex.getMessage());
		errors.setCode(HttpStatus.BAD_REQUEST.toString());
		Response response = new Response();
		response.setError(errors);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<?> handleNoSuchElementException(Exception ex, WebRequest request) {
		Error errors = new Error();
		errors.setErrorList((Stream.of(ex.getMessage().split(",")).collect(Collectors.toList())));
		errors.setReason(ex.getMessage());
		errors.setCode(HttpStatus.BAD_REQUEST.toString());
		Response response = new Response();
		response.setError(errors);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

//	@ExceptionHandler(RuntimeException.class)
//	public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
//		Error errors = new Error();
//		errors.setErrorList((Stream.of(ex.getMessage().split(",")).collect(Collectors.toList())));
//		errors.setReason(ex.getMessage());
//		errors.setCode(HttpStatus.CONFLICT.toString());
//		Response response = new Response();
//		response.setError(errors);
//		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
//		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
		Error errors = new Error();
		errors.setErrorList((Stream.of(ex.getMessage().split(",")).collect(Collectors.toList())));
		errors.setReason(ex.getMessage());
		errors.setCode(HttpStatus.CONFLICT.toString());
		Response response = new Response();
		response.setError(errors);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex) {
		Error errors = new Error();
		errors.setErrorList((Stream.of(ex.getMessage().split(",")).collect(Collectors.toList())));
		errors.setReason(ex.getMessage());
		errors.setCode(HttpStatus.CONFLICT.toString());
		Response response = new Response();
		response.setError(errors);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<Object> handleSignatureException(SignatureException ex) {
		Error errors = new Error();
		errors.setErrorList((Stream.of(ex.getMessage().split(",")).collect(Collectors.toList())));
		errors.setReason(ex.getMessage());
		errors.setCode(HttpStatus.UNAUTHORIZED.toString());
		Response response = new Response();
		response.setError(errors);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

//	@ExceptionHandler(HttpMessageNotReadableException.class)
//	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
//			WebRequest request) {
//		Error errors = new Error();
//		errors.setCode(HttpStatus.BAD_REQUEST.toString());
//
//		String errorMessage = "Failed to read request";
//		if (ex.getCause() instanceof InvalidFormatException) {
//			InvalidFormatException ife = (InvalidFormatException) ex.getCause();
//			errorMessage = "Invalid value for field: " + ife.getPath().get(0).getFieldName();
//		} else if (ex.getMessage().contains("Invalid role")) {
//			errorMessage = ex.getMessage();
//		}
//		errors.setReason(errorMessage);
//		Response response = new Response();
//		response.setError(errors);
//		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
//		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
			WebRequest request) {

		Map<String, Object> body = new HashMap<>();
		body.put("type", "about:blank");
		body.put("title", "Bad Request");
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("detail", "Invalid request payload: " + ex.getLocalizedMessage());
		body.put("instance", request.getDescription(false));

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
