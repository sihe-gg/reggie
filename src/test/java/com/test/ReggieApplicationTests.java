package com.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReggieApplicationTests {

	@Test
	void contextLoads() {
		//String backend = this.getClass().getClassLoader().getResource("backend/images/dish").getPath();
		//System.out.println(backend);

        String suffix = "123.exe";
		suffix = suffix.substring(suffix.lastIndexOf("."));
		System.out.println(suffix);
	}

}
