package com.publicholidaysbycountry.country.infrastructure;

import com.publicholidaysbycountry.country.application.CountryRepository;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.exception.CountryNotFoundException;
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

    @Override
    public void deleteAll() {
        countryJpaRepository.deleteAllInBatch();
    }

    @Override
    public Country findByCode(String countryCode) {
        CountryEntity countryEntity = countryJpaRepository.findByCode(countryCode)
                .orElseThrow(() -> new CountryNotFoundException("존재하지 않는 국가 코드입니다: " + countryCode));
        return countryEntity.toCountry();
    }
}
