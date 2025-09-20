package com.laba;

import com.laba.amqp.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(Config.class)
public class CatsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatsServiceApplication.class, args);
    }
}
