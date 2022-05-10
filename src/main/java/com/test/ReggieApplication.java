package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan	// 过滤器注解扫描
@EnableTransactionManagement
@EnableCaching
public class ReggieApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggieApplication.class, args);
	}

}
