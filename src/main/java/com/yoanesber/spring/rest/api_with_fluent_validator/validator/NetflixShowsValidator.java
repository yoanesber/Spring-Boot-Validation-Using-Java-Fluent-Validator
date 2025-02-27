package com.yoanesber.spring.rest.api_with_fluent_validator.validator;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringMatches;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeLessThanOrEqual;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.yoanesber.spring.rest.api_with_fluent_validator.dto.NetflixShowsDTO;
import com.yoanesber.spring.rest.api_with_fluent_validator.entity.EShowType;

import br.com.fluentvalidator.AbstractValidator;

public class NetflixShowsValidator extends AbstractValidator<NetflixShowsDTO> {
    @Override
    public void rules() {
        String enumRegex = Arrays.stream(EShowType.values())
                         .map(Enum::name)
                         .collect(Collectors.joining("|", "^(", ")$"));

        String showTypes = Arrays.stream(EShowType.values())
                         .map(Enum::name)
                         .collect(Collectors.joining(" or ", "", ""));

        ruleFor(NetflixShowsDTO::getShowType)
            .must(not(stringEmptyOrNull()))
                .withMessage("ShowType must not be null or empty")
                .withFieldName("ShowType")
            .must(type -> type != null && type.matches(enumRegex))
                .when(not(stringEmptyOrNull()))
                .withMessage("ShowType must be either " + showTypes)
                .withFieldName("ShowType");

        ruleFor(NetflixShowsDTO::getTitle)
            .must(not(stringEmptyOrNull()))
                .withMessage("Title must not be null or empty")
                .withFieldName("Title")
            .must(stringMatches("^[\\x20-\\x7E]+$"))
                .when(not(stringEmptyOrNull()))
                .withMessage("Title must contain only printable ASCII characters")
                .withFieldName("Title");

        ruleFor(NetflixShowsDTO::getDirector)
            .must(stringMatches("^[\\x20-\\x7E]+$"))
                .when(not(stringEmptyOrNull()))
                .withMessage("Director must contain only printable ASCII characters")
                .withFieldName("Director");

        ruleFor(NetflixShowsDTO::getCastMembers)
            .must(stringMatches("^[\\x20-\\x7E]+$"))
                .when(not(stringEmptyOrNull()))
                .withMessage("Cast members must contain only printable ASCII characters")
                .withFieldName("CastMembers");

        ruleFor(NetflixShowsDTO::getCountry)
            .must(not(stringEmptyOrNull()))
                .withMessage("Country must not be null or empty")
                .withFieldName("Country")
            .must(stringMatches("^[\\x20-\\x7E]+$"))
                .when(not(stringEmptyOrNull()))
                .withMessage("Country must contain only printable ASCII characters")
                .withFieldName("Country")
            .must(stringSizeLessThanOrEqual(60))
                .when(not(stringEmptyOrNull()))
                .withMessage("Country must be less than or equal to 60 character length")
                .withFieldName("Country");

        ruleFor(NetflixShowsDTO::getDateAdded)
            .must(not(nullValue()))
                .withMessage("DateAdded must not be null or empty")
                .withFieldName("DateAdded");

        ruleFor(NetflixShowsDTO::getReleaseYear)
            .must(not(nullValue()))
                .withMessage("ReleaseYear must not be null or empty")
                .withFieldName("ReleaseYear");

        ruleFor(NetflixShowsDTO::getRating)
            .must(this::checkRatingRange)
                .when(not(nullValue()))
                .withMessage("Rating must be between 1 and 10")
                .withFieldName("Rating");

        ruleFor(NetflixShowsDTO::getListedIn)
            .must(stringMatches("^[\\x20-\\x7E]+$"))
                .when(not(stringEmptyOrNull()))
                .withMessage("ListedIn must contain only printable ASCII characters")
                .withFieldName("ListedIn");

        // ruleFor(NetflixShowsDTO::getDescription)
        //     .must(stringMatches("^[\\x20-\\x7E]+$"))
        //         .when(not(stringEmptyOrNull()))
        //         .withMessage("Description must contain only printable ASCII characters")
        //         .withFieldName("Description");
    }

    private Boolean checkRatingRange(Integer rating) {
        if (rating == null) {
            return false;
        }

        return rating >= 1 && rating <= 10;
    }
}
