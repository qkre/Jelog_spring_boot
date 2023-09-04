package com.example.jelog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://10.11.9.25:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false)
                .maxAge(3000);
    }
}
