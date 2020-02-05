package com.kakaocorp.iamguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class IamGuideApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(IamGuideApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
<<<<<<< HEAD
        return builder.sources(IamGuideApplication.class);
=======

        return builder.sources(IamGuideApplication.class);

>>>>>>> 4379b25e5d0a8abb51f2a8f3cd7ae9508a028bde
    }
}
