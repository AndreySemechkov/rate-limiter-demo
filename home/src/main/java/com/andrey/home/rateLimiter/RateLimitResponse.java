package com.andrey.home.rateLimiter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateLimitResponse {
	private boolean block;
}