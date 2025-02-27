package com.yoanesber.spring.rest.api_with_fluent_validator.service;

import java.util.List;

import com.yoanesber.spring.rest.api_with_fluent_validator.dto.NetflixShowsDTO;

public interface NetflixShowsService {
    // Create NetflixShows
    NetflixShowsDTO createNetflixShows(NetflixShowsDTO netflixShowsDTO);

    // Get all NetflixShows
    List<NetflixShowsDTO> getAllNetflixShows();

    // Get NetflixShows by id
    NetflixShowsDTO getNetflixShowsById(Long id);

    // Update NetflixShows
    NetflixShowsDTO updateNetflixShows(Long id, NetflixShowsDTO netflixShowsDTO);

    // Delete NetflixShows
    Boolean deleteNetflixShows(Long id);
}
