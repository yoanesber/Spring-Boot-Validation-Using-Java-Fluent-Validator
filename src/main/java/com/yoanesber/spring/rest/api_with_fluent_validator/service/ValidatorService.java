package com.yoanesber.spring.rest.api_with_fluent_validator.service;

import br.com.fluentvalidator.context.ValidationResult;

import java.util.List;
import java.util.Map;

import com.yoanesber.spring.rest.api_with_fluent_validator.dto.NetflixShowsDTO;

public interface ValidatorService {
    // Get the error list from ValidationResult
    Map<String, List<String>> getErrorList(ValidationResult result);

    // Validate NetflixShowsDTO
    ValidationResult validateNetflixShows(NetflixShowsDTO netflixShowsDTO);
}
