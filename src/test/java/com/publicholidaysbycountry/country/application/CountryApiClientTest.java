package com.publicholidaysbycountry.country.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import com.publicholidaysbycountry.country.application.dto.CountryDTO;
import com.publicholidaysbycountry.global.exception.CountryNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class CountryApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryApiClient countryApiClient;

    @Test
    @DisplayName("정상적으로 CountryDTO 배열을 반환한다")
    void getCountryApiRequest_success() {
        // given
        CountryDTO[] response = new CountryDTO[] {
                new CountryDTO("KR", "대한민국"),
                new CountryDTO("US", "미국")
        };

        given(restTemplate.getForObject(anyString(), eq(CountryDTO[].class)))
                .willReturn(response);

        // when
        CountryDTO[] result = countryApiClient.getCountryApiRequest();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result[0].countryCode()).isEqualTo("KR");
        assertThat(result[1].name()).isEqualTo("미국");
    }

    @Test
    @DisplayName("국가 정보를 찾을 수 없으면 예외를 던진다")
    void getCountryApiRequest_throwExceptionIfNull() {
        // given
        given(restTemplate.getForObject(anyString(), eq(CountryDTO[].class)))
                .willReturn(null);

        // when & then
        assertThatThrownBy(() -> countryApiClient.getCountryApiRequest())
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("국가 목록 정보를 찾을 수 없습니다.");
    }
}