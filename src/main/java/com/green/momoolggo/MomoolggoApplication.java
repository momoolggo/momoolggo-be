package com.green.momoolggo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MomoolggoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomoolggoApplication.class, args);
    }

}
