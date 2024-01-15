package com.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired private ServerPortService serverPortService;

	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;


	@Test
	public void getPortTest(){


		int port = serverPortService.getPort();
        System.out.println("--------------------------------- "+ port);
	}




	@Test
	public void testAddUser(){











	}
}
