package com.yoanesber.spring.rest.api_with_fluent_validator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yoanesber.spring.rest.api_with_fluent_validator.entity.NetflixShows;

@Repository
public interface NetflixShowsRepository extends JpaRepository<NetflixShows, Long> {

}
