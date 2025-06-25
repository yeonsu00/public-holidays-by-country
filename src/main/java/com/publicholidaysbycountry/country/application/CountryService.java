package com.publicholidaysbycountry.country.application;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import com.publicholidaysbycountry.country.domain.Country;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryApiClient countryApiClient;

    @Transactional
    public List<Country> saveCountries() {
        List<CountryDTO> countryDTOS = List.of(countryApiClient.getCountryApiRequest());
        List<Country> countries = CountryDTO.toCounties(countryDTOS);

        return countryRepository.saveAll(countries);
    }

    public void deleteAllCountries() {
        countryRepository.deleteAll();
    }
}
