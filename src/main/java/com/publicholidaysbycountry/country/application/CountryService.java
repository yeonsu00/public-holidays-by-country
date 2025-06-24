package com.publicholidaysbycountry.country.application;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;

    public List<Country> saveCountries() {
        List<CountryDTO> countryDTOS = getApiRequest();
        List<Country> countries = CountryDTO.toCounties(countryDTOS);

        return countryRepository.saveAll(countries);
    }

    private List<CountryDTO> getApiRequest() {
        CountryDTO[] countryDTOS = restTemplate.getForObject(Constants.AVAILABLE_COUNTRIES_API_URL, CountryDTO[].class);

        return List.of(countryDTOS);
    }
}
