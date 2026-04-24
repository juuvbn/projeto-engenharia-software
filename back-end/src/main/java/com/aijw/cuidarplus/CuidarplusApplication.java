package com.aijw.cuidarplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CuidarplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuidarplusApplication.class, args);
    }

}
