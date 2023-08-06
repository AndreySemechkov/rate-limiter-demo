package com.andrey.home;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.andrey.home.rateLimiter.TimestampedCounter;
import com.andrey.home.rateLimiter.TimestampedCounterFactory;
import com.andrey.home.rateLimiter.UrlVisitsLimiter;



@SpringBootTest
@TestPropertySource(properties = {"app.ttl=1000000000000000","app.threshold=101"})
class HomeAssignmentApplicationTests {
	
	@Autowired
	UrlVisitsLimiter requestsCounter;
	
	@Autowired
	TimestampedCounterFactory timestampedCounterFactory;
	
	@ParameterizedTest
	@CsvSource({"10, false", "50, false", "100, true", "200, true"})
	public void testConcurrentIncrementAndGet(int TASKS_NUMBER, boolean isBlock) {
    	// multiple threads concurently incrementing requets counter TASKS_NUMBER times
		CountDownLatch latch = new CountDownLatch(TASKS_NUMBER);
    	ExecutorService executorService = Executors.newFixedThreadPool(8);
		// reseting counter singleton between parametrized tests
		requestsCounter.put("abc.com", 0L);

    	for (int i = 0; i < TASKS_NUMBER; i++) {
    	    executorService.submit(() -> {
    	        requestsCounter.incrementAndCheck("abc.com");
    	        latch.countDown();
    	    });
    	}
    	try {
			latch.await();
		} catch (InterruptedException e) {
			// in case of interuption test will fail
			e.printStackTrace();
			assertTrue(false);
		}
    	long count = requestsCounter.get("abc.com");
    	assertEquals(TASKS_NUMBER, count);
		assertEquals(requestsCounter.incrementAndCheck("abc.com"), isBlock);
    	executorService.shutdown();
	}

	@Test
	public void testCounterIncrement() {
    	TimestampedCounter counter = timestampedCounterFactory.getTimestampedCounter(0L);
    	long initialCount = counter.get();
		int TASKS_NUMBER = 100;
		CountDownLatch latch = new CountDownLatch(TASKS_NUMBER);
    	ExecutorService executorService = Executors.newFixedThreadPool(8);

    	for (int i = 0; i < TASKS_NUMBER; i++) {
    	    executorService.submit(() -> {
    	        counter.incrementAndGet();
    	        latch.countDown();
    	    });
    	}
    	try {
			latch.await();
		} catch (InterruptedException e) {
			// in case of interuption test will fail
			e.printStackTrace();
			assertTrue(false);
		}
    	long laterTime = Instant.now().toEpochMilli();
		assertEquals(initialCount + TASKS_NUMBER, counter.get());
		assertNotEquals(laterTime, counter.getCreatedAt());
		executorService.shutdown();
	}
}
