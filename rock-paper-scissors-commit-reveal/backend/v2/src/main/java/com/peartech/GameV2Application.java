package com.peartech;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameV2Application {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(GameV2Application.class, args);
    }

}
