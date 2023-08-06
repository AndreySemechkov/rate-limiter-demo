package com.andrey.home.rateLimiter;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Url {

    @NotBlank(message = "Bad Request: Non empty URL is mandatory")
	private String url;
}
