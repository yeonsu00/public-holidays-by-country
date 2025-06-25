package com.publicholidaysbycountry.holiday.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HolidayTest {

    @DisplayName("counties가 존재한다.")
    @Test
    void hasCounties_true() {
        // given
        Holiday holiday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("새해")
                .name("new year")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .counties(List.of("Country1", "Country2"))
                .types(List.of(HolidayType.PUBLIC))
                .build();

        // when
        boolean result = holiday.hasCounties();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("counties가 비어있다.")
    @Test
    void hasCounties_false() {
        // given
        Holiday holiday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("새해")
                .name("new year")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .types(List.of(HolidayType.PUBLIC))
                .build();

        // when
        boolean result = holiday.hasCounties();

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("type가 존재한다.")
    @Test
    void hasTypes_true() {
        // given
        Holiday holiday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("새해")
                .name("new year")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .counties(List.of("Country1", "Country2"))
                .types(List.of(HolidayType.PUBLIC))
                .build();

        // when
        boolean result = holiday.hasTypes();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("type가 비어있다..")
    @Test
    void hasTypes_false() {
        // given
        Holiday holiday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("새해")
                .name("new year")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .counties(List.of("Country1", "Country2"))
                .build();

        // when
        boolean result = holiday.hasTypes();

        // then
        assertThat(result).isFalse();
    }

}