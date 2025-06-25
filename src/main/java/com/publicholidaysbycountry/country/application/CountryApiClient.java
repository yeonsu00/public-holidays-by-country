package com.publicholidaysbycountry.country.application;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.exception.CountryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CountryApiClient {

    private final RestTemplate restTemplate;

    public CountryDTO[] getCountryApiRequest() {
        CountryDTO[] countryDTOS = restTemplate.getForObject(Constants.AVAILABLE_COUNTRIES_API_URL, CountryDTO[].class);

        if (countryDTOS == null) {
            throw new CountryNotFoundException("국가 목록 정보를 찾을 수 없습니다.");
        }

        return countryDTOS;
    }

}
