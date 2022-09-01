package com.ouni;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ouni.mapper")
public class ApplicationSpring {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationSpring.class,args);
    }
}
