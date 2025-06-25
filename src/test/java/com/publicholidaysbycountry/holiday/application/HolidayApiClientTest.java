package com.publicholidaysbycountry.holiday.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.exception.HolidayNotFoundException;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class HolidayApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HolidayApiClient holidayApiClient;

    @Test
    @DisplayName("정상적으로 공휴일 DTO 배열을 반환한다")
    void getHolidayApiRequest_success() {
        // given
        Country country = Country.builder()
                .code("KR")
                .name("대한민국")
                .build();
        int year = 2025;

        HolidayDTO[] response = new HolidayDTO[]{
                new HolidayDTO(
                        LocalDate.of(2025, 1, 1), "새해", "New Year's Day",
                        "KR", true, true, null, null, null
                )
        };

        when(restTemplate.getForObject(anyString(), eq(HolidayDTO[].class)))
                .thenReturn(response);

        // when
        HolidayDTO[] result = holidayApiClient.getHolidayApiRequest(country, year);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result[0].localName()).isEqualTo("새해");

    }

    @Test
    @DisplayName("공휴일 정보를 찾을 수 없으면 예외를 던진다")
    void getHolidayApiRequest_throwExceptionIfNull() {
        // given
        Country country = Country.builder()
                .code("KR")
                .name("대한민국")
                .build();
        int year = 2025;

        when(restTemplate.getForObject(anyString(), eq(HolidayDTO[].class)))
                .thenReturn(null);

        // when & then
        assertThatThrownBy(() -> holidayApiClient.getHolidayApiRequest(country, year))
                .isInstanceOf(HolidayNotFoundException.class)
                .hasMessageContaining("2025년도 대한민국의 공휴일 정보를 찾을 수 없습니다.");
    }
}
