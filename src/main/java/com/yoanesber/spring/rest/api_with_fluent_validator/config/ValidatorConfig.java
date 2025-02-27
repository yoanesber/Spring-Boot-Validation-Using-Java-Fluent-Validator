package com.yoanesber.spring.rest.api_with_fluent_validator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yoanesber.spring.rest.api_with_fluent_validator.validator.NetflixShowsValidator;

@Configuration
public class ValidatorConfig {
    // Create a bean for NetflixShowsValidator
    @Bean
    public NetflixShowsValidator netflixShowsValidator() {
        return new NetflixShowsValidator();
    }
}
