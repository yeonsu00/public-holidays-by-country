package com.publicholidaysbycountry.country.application;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.exception.CountryNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public List<Country> saveCountries() {
        List<CountryDTO> countryDTOS = getApiRequest();
        List<Country> countries = CountryDTO.toCounties(countryDTOS);

        return countryRepository.saveAll(countries);
    }

    private List<CountryDTO> getApiRequest() {
        CountryDTO[] countryDTOS = restTemplate.getForObject(Constants.AVAILABLE_COUNTRIES_API_URL, CountryDTO[].class);

        if (countryDTOS == null) {
            throw new CountryNotFoundException("국가 목록 정보를 찾을 수 없습니다.");
        }

        return List.of(countryDTOS);
    }

    public void deleteAllCountries() {
        countryRepository.deleteAll();
    }
}
