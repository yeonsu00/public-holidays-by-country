package com.publicholidaysbycountry.country.domain;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Country {

    private Long countryId;
    private String code;
    private String name;

    @Builder
    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Country fromCountryDTO(CountryDTO countryDTO) {
        return Country.builder()
                .code(countryDTO.countryCode())
                .name(countryDTO.name())
                .build();
    }
}
