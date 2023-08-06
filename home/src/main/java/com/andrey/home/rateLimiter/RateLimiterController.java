package com.andrey.home.rateLimiter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;


import jakarta.validation.Valid;


@RestController
public class RateLimiterController {

	@Autowired
	private UrlVisitsLimiter urlVisitsLimiter;


	@Operation(summary = "Report if URL is blocked by rate limiter to be requested")
	@PostMapping(path="/report", consumes = "application/json", produces = "application/json")
	public RateLimitResponse reportIsAllowed(@RequestBody @Valid Url url) {

		Boolean isBlocked = urlVisitsLimiter.incrementAndCheck(url.getUrl());
		return new RateLimitResponse(isBlocked);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new ConcurrentHashMap<>();
    
		exception.getBindingResult().getAllErrors().forEach((error) -> {
        	String fieldName = ((FieldError) error).getField();
        	String errorMessage = error.getDefaultMessage();
        	errors.put(fieldName, errorMessage);
    	});
    	return errors;
	}
}