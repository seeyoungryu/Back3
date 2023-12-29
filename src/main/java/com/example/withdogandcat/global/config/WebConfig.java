package com.example.withdogandcat.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String REACT_LOCAL_HOST = "http://localhost:5173";
    private static final String REACT_LOCAL_HOST2 = "http://localhost:5174";
    private static final String REACT_LOCAL_HOST3 = "http://localhost:5175";
    private static final String PROD_HOST = "https://final-pi-coral.vercel.app";
    private static final String PROD_HOST2 = "https://final-dyh-dyeons-projects.vercel.app";
    private static final String PROD_HOST3 = "https://final-sub-test-ver.vercel.app";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(REACT_LOCAL_HOST, REACT_LOCAL_HOST2,
                        REACT_LOCAL_HOST3, PROD_HOST, PROD_HOST2, PROD_HOST3)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }
}
