package com.example.withdogandcat.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String REACT_LOCAL_HOST = "http://localhost:5173";
    private static final String REACT_LOCAL_HOST2 = "http://localhost:5174";
    private static final String REACT_LOCAL_HOST3 = "https://final-pi-coral.vercel.app";

    private static final String PRODUCTION_HOST = "http://3.37.121.136:8080";
    private static final String PRODUCTION_HOST2 = "http://3.37.121.136:443";
    private static final String PRODUCTION_HOST3 = "https://warrwarr.co.kr";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(REACT_LOCAL_HOST, REACT_LOCAL_HOST2, REACT_LOCAL_HOST3,
                        PRODUCTION_HOST, PRODUCTION_HOST2, PRODUCTION_HOST3)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }

}
