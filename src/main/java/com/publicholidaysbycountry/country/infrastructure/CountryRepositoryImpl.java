package com.publicholidaysbycountry.country.infrastructure;

import com.publicholidaysbycountry.country.application.CountryRepository;
import com.publicholidaysbycountry.country.domain.Country;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final CountryJpaRepository countryJpaRepository;

    @Override
    public List<Country> saveAll(List<Country> countries) {
        List<CountryEntity> countryEntities = countries.stream()
                .map(CountryEntity::fromCountry)
                .toList();
        List<CountryEntity> savedCountryEntities = countryJpaRepository.saveAll(countryEntities);

        return savedCountryEntities.stream()
                .map(CountryEntity::toCountry)
                .toList();
    }
}
