package com.peartech;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RockPaperScissorsV1 {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(RockPaperScissorsV1.class, args);
    }

}
