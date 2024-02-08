package com.example;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@EnableSwagger2Doc
@SpringBootApplication
public class ContentApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplicaiton.class,args);
    }
}
