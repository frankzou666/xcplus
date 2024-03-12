package com.example.xcpluscontentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
@EnableFeignClients(basePackages = {"com.example.xcpluscontentservice.content.feignclient"})
class XcplusContentServiceApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(XcplusContentServiceApplicationTests.class,args);
	}

}
