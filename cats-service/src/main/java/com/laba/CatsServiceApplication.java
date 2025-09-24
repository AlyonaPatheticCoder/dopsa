package com.laba;

import com.laba.amqp.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@Import(Config.class)
public class CatsServiceApplication {
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
    public static void main(String[] args) {
        SpringApplication.run(CatsServiceApplication.class, args);
    }
}
