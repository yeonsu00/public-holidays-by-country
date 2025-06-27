package com.publicholidaysbycountry.country.application;

import static org.assertj.core.api.Assertions.*;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.country.infrastructure.CountryJpaRepository;
import com.publicholidaysbycountry.country.infrastructure.CountryRepositoryImpl;
import com.publicholidaysbycountry.global.exception.CountryNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(CountryRepositoryImpl.class)
class CountryRepositoryTest {

    @Autowired
    private CountryJpaRepository countryJpaRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("국가 리스트를 저장 후 저장된 국가 리스트를 반환한다.")
    void saveAll_success() {
        // given
        Country kr = Country.builder().code("KR").name("Korea").build();
        Country us = Country.builder().code("US").name("United States").build();

        // when
        List<Country> savedCountries = countryRepository.saveAll(List.of(kr, us));

        // then
        assertThat(savedCountries).hasSize(2);
        assertThat(savedCountries).extracting(Country::getCode).containsExactlyInAnyOrder("KR", "US");
    }

    @Test
    @DisplayName("저장된 모든 국가를 삭제한다.")
    void deleteAll_success() {
        // given
        countryRepository.saveAll(List.of(
                Country.builder().code("KR").name("Korea").build()
        ));

        // when
        countryRepository.deleteAll();

        // then
        assertThat(countryJpaRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("국가 코드로 조회한다.")
    void findByCode_success() {
        // given
        countryRepository.saveAll(List.of(
                Country.builder().code("KR").name("Korea").build()
        ));

        // when
        Country result = countryRepository.findByCode("KR");

        // then
        assertThat(result.getName()).isEqualTo("Korea");
    }

    @Test
    @DisplayName("존재하지 않는 국가 코드로 조회 시 예외 발생한다.")
    void findByCode_fail() {
        // when & then
        assertThatThrownBy(() -> countryRepository.findByCode("ZZ"))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("존재하지 않는 국가 코드입니다");
    }
}

