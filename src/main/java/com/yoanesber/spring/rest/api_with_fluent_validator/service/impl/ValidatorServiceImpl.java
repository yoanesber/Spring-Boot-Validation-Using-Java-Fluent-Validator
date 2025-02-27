package com.yoanesber.spring.rest.api_with_fluent_validator.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.fluentvalidator.context.ValidationResult;

import com.yoanesber.spring.rest.api_with_fluent_validator.dto.NetflixShowsDTO;
import com.yoanesber.spring.rest.api_with_fluent_validator.service.ValidatorService;
import com.yoanesber.spring.rest.api_with_fluent_validator.validator.NetflixShowsValidator;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    private final NetflixShowsValidator netflixShowsValidator;

    public ValidatorServiceImpl(NetflixShowsValidator netflixShowsValidator) {
        this.netflixShowsValidator = netflixShowsValidator;
    }

    @Override
    public Map<String, List<String>> getErrorList(ValidationResult result) {
        // Group the errors by field
        return new TreeMap<>(result.getErrors().stream().collect(Collectors.groupingBy(e -> e.getField(), 
            Collectors.mapping(e -> e.getMessage(), Collectors.toList()))));
    }

    @Override
    public ValidationResult validateNetflixShows(NetflixShowsDTO netflixShowsDTO) {
        Assert.notNull(netflixShowsDTO, "NetflixShowsDTO must not be null");

        try {
            return netflixShowsValidator.validate(netflixShowsDTO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate NetflixShows: " + e.getMessage());
        }
    }

}
