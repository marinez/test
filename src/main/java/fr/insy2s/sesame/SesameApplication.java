package fr.insy2s.sesame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SesameApplication {

    public static void main(String[] args) {
        SpringApplication.run(SesameApplication.class, args);
    }

}
