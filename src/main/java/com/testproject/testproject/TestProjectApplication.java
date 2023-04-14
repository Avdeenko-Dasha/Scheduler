package com.testproject.testproject;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;



@SpringBootApplication
public class TestProjectApplication{

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestProjectApplication.class)
            .headless(false)
            .web(WebApplicationType.NONE)
            .run(args);
    }

}
