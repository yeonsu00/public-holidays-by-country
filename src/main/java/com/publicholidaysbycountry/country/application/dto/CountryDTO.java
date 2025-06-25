package com.publicholidaysbycountry.country.application.dto;

import com.publicholidaysbycountry.country.domain.Country;
import java.util.List;

public record CountryDTO(
    String countryCode,
    String name
) {

    public static List<Country> toCounties(List<CountryDTO> countryDTOs) {
        return countryDTOs.stream()
                .map(Country::fromCountryDTO)
                .toList();
    }

}
