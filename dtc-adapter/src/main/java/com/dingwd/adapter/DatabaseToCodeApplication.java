package com.dingwd.adapter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dingwd")
@MapperScan("com.dingwd.**.repository")
public class DatabaseToCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseToCodeApplication.class, args);
    }
}
