package org.yukung.sandbox.jooq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class JooqApplication {
    public static void main(String[] args) {
        SpringApplication.run(JooqApplication.class, args);
    }
}
