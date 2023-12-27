package com.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired private ServerPortService serverPortService;




	@Test
	public void getPortTest(){


		int port = serverPortService.getPort();
        System.out.println("--------------------------------- "+ port);
	}


	@Test
	public void testAddUser(){











	}
}
