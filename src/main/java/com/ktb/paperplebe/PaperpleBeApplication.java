package com.ktb.paperplebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaperpleBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperpleBeApplication.class, args);
    }

}
