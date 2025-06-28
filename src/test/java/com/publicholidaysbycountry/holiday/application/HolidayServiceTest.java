package com.publicholidaysbycountry.holiday.application;

import static org.assertj.core.api.Assertions.*;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import com.publicholidaysbycountry.holiday.presentation.response.HolidayResponseDTO;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        Holiday h1 = Holiday.builder()
                .date(LocalDate.of(2024, 1, 1))
                .localName("새해")
                .name("New Year's Day")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .launchYear(null)
                .build();

        Holiday h2 = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("New Year's Day")
                .name("New Year's Day")
                .countryCode("US")
                .fixed(true)
                .global(true)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .launchYear(null)
                .build();

        holidayRepository.save(List.of(h1, h2));
    }

    @DisplayName("공휴일 리스트를 저장하면 저장된 개수를 반환한다.")
    @Test
    void saveHolidays() {
        // given
        Holiday h3 = Holiday.builder()
                .date(LocalDate.of(2023, 1, 1))
                .localName("새해")
                .name("New Year's Day")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .launchYear(null)
                .build();

        Holiday h4 = Holiday.builder()
                .date(LocalDate.of(2022, 1, 1))
                .localName("New Year's Day")
                .name("New Year's Day")
                .countryCode("US")
                .fixed(true)
                .global(true)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .launchYear(null)
                .build();

        List<Holiday> holidays = List.of(h3, h4);

        // when
        int savedCount = holidayService.saveHolidays(holidays);

        // then
        assertThat(savedCount).isEqualTo(2);
        Page<Holiday> savedPage = holidayRepository.findAll(PageRequest.of(0, 10));
        List<Holiday> saved = savedPage.getContent();
        assertThat(saved).hasSize(4);
    }


    @Test
    @DisplayName("year, countryCode 조건에 맞는 공휴일을 조회한다.")
    void getHolidaysByYearAndCountry() {
        // given
        List<Integer> years = List.of(2024, 2025);
        List<String> countryCodes = List.of("KR", "US");
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<HolidayResponseDTO> result = holidayService.getHolidaysByYearAndCountry(years, countryCodes, pageable);

        // then
        assertThat(result).isNotEmpty();
        result.forEach(dto -> {
            assertThat(countryCodes).contains(dto.countryCode());
        });
        assertThat(result.getContent()).hasSize(2);
    }

    @DisplayName("year 조건에 맞는 공휴일을 조회한다.")
    @Test
    void getHolidaysByYear() {
        // given
        List<Integer> years = List.of(2024);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<HolidayResponseDTO> result = holidayService.getHolidaysByYear(years, pageable);

        // then
        assertThat(result).isNotEmpty();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        result.forEach(dto -> {
            LocalDate date = LocalDate.parse(dto.date(), formatter);
            assertThat(date.getYear()).isEqualTo(2024);
        });

        assertThat(result.getContent()).hasSize(1);
    }

    @DisplayName("country 조건에 맞는 공휴일을 조회한다.")
    @Test
    void getHolidaysByCountry() {
        // given
        List<String> countryCodes = List.of("KR");
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<HolidayResponseDTO> result = holidayService.getHolidaysByCountry(countryCodes, pageable);

        // then
        assertThat(result).isNotEmpty();
        result.forEach(dto -> assertThat(dto.countryCode()).isEqualTo("KR"));
        assertThat(result.getContent()).hasSize(1);
    }

    @DisplayName("전체 공휴일을 조회하면 페이지 결과를 반환한다.")
    @Test
    void getAllHolidays_returnsPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        var result = holidayService.getAllHolidays(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isInstanceOf(List.class);
    }

    @DisplayName("기존 데이터와 동일한 키를 가진 공휴일이 주어지면 해당 공휴일를 update한다.")
    @Test
    void refreshHolidaysByYearAndCountry_update() {
        Holiday updatedHoliday = Holiday.builder()
                .date(LocalDate.of(2024, 1, 1))
                .localName("새해")
                .name("New Year's Day")
                .countryCode("KR")
                .fixed(false)
                .global(false)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .launchYear(2000)
                .build();

        // when
        int updatedCount = holidayService.refreshHolidaysByYearAndCountry(List.of(updatedHoliday));

        // then
        assertThat(updatedCount).isEqualTo(1);

        em.flush();
        em.clear();
        Holiday result = holidayRepository.findAll(PageRequest.of(0, 10))
                .getContent().stream()
                .filter(h -> h.getDate().equals(LocalDate.of(2024, 1, 1)) && h.getCountryCode().equals("KR"))
                .findFirst()
                .orElseThrow();

        assertThat(result.isFixed()).isFalse();
        assertThat(result.isGlobal()).isFalse();
        assertThat(result.getLaunchYear()).isEqualTo(2000);
    }

    @DisplayName("기존과 다른 키를 가진 공휴일이 주어지면 새롭게 insert한다.")
    @Test
    void refreshHolidaysByYearAndCountry_insert() {
        // given
        Holiday newHoliday = Holiday.builder()
                .date(LocalDate.of(2023, 12, 25))
                .localName("Christmas Day")
                .name("Christmas Day")
                .countryCode("AD")
                .fixed(true)
                .global(true)
                .launchYear(null)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .build();

        // when
        int insertedCount = holidayService.refreshHolidaysByYearAndCountry(List.of(newHoliday));

        // then
        assertThat(insertedCount).isEqualTo(1);

        Page<Holiday> holidayPage = holidayRepository.findAll(PageRequest.of(0, 10));
        List<Holiday> holidays = holidayPage.getContent();
        assertThat(holidays).hasSize(3);

        Holiday inserted = holidays.stream()
                .filter(h -> h.getCountryCode().equals("AD"))
                .findFirst()
                .orElseThrow();

        assertThat(inserted.getName()).isEqualTo("Christmas Day");
    }

    @DisplayName("필터 조건에 따라 공휴일을 조회한다.")
    @Test
    void getHolidaysByFilter() {
        // given
        Holiday h3 = Holiday.builder()
                .date(LocalDate.of(2020, 1, 1))
                .localName("새해")
                .name("New Year's Day")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .counties(List.of("Seoul"))
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("Seoul")
                .launchYear(2000)
                .build();

        Holiday h4 = Holiday.builder()
                .date(LocalDate.of(2025, 12, 25))
                .localName("크리스마스")
                .name("Christmas Day")
                .countryCode("US")
                .fixed(false)
                .global(false)
                .counties(null)
                .types(List.of(HolidayType.BANK))
                .countiesKey("")
                .launchYear(1990)
                .build();

        holidayRepository.save(List.of(h3, h4));

        em.flush();
        em.clear();

        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2025, 12, 31);
        List<String> types = List.of("PUBLIC");
        Boolean hasCounty = true;
        Boolean fixed = true;
        Boolean global = true;
        Integer launchYear = 2000;
        List<String> countryCode = List.of("KR");

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<HolidayResponseDTO> result = holidayService.getHolidaysByFilter(
                from, to, types, hasCounty, fixed, global, launchYear, countryCode, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        HolidayResponseDTO dto = result.getContent().getFirst();
        assertThat(dto.date()).isEqualTo("2020-01-01");
        assertThat(dto.countryCode()).isEqualTo("KR");
        assertThat(dto.types()).contains(HolidayType.PUBLIC);
    }

    @Test
    @DisplayName("year와 country로 해당하는 공휴일만 삭제한다.")
    void deleteHolidaysByYearAndCountry() {
        // given
        Country us = new Country("US", "United States");

        // when
        int deletedCount = holidayService.deleteHolidaysByYearAndCountry(2025, us);

        // then
        assertThat(deletedCount).isEqualTo(1);
    }

}

