package com.publicholidaysbycountry.country.application;

import static org.assertj.core.api.Assertions.*;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import com.publicholidaysbycountry.country.domain.Country;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryApiClient countryApiClient;

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("외부 API에서 받아온 국가 정보를 저장한다.")
    void saveCountries_success() {
        // given
        CountryDTO[] mockCountryDTOResponse = new CountryDTO[]{
                new CountryDTO("KR", "Korea"),
                new CountryDTO("US", "United States")
        };
        given(countryApiClient.getCountryApiRequest()).willReturn(mockCountryDTOResponse);

        List<Country> mockCountryEntities = List.of(
                Country.builder().code("KR").name("Korea").build(),
                Country.builder().code("US").name("United States").build()
        );

        given(countryRepository.saveAll(anyList())).willReturn(mockCountryEntities);

        // when
        List<Country> savedCountries = countryService.saveCountries();

        // then
        assertThat(savedCountries).hasSize(2);
        assertThat(savedCountries).extracting(Country::getCode)
                .containsExactlyInAnyOrder("KR", "US");
    }

    @Test
    @DisplayName("deleteAllCountries 호출 시 deleteAll이 실행된다.")
    void deleteAllCountries_callsDeleteAll() {
        // when
        countryService.deleteAllCountries();

        // then
        then(countryRepository).should(times(1)).deleteAll();
    }

    @Test
    @DisplayName("주어진 countryCode로 findByCode 호출 시 일치하는 Country가 반환된다.")
    void getCountryByCode_returnsCorrectCountry() {
        // given
        String code = "KR";
        Country expectedCountry = Country.builder()
                .code(code)
                .name("Korea")
                .build();
        given(countryRepository.findByCode(code)).willReturn(expectedCountry);

        // when
        Country country = countryService.getCountryByCode(code);

        // then
        assertThat(country).isNotNull();
        assertThat(country.getCode()).isEqualTo(code);
        assertThat(country.getName()).isEqualTo("Korea");
    }
}

