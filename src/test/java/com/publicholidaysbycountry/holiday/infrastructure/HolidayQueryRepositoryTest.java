package com.publicholidaysbycountry.holiday.infrastructure;

import static org.assertj.core.api.Assertions.*;

import com.publicholidaysbycountry.holiday.domain.HolidayType;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import java.time.LocalDate;
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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HolidayQueryRepositoryTest {

    @Autowired
    private HolidayQueryRepository holidayQueryRepository;

    @Autowired
    private HolidayJpaRepository holidayJpaRepository;

    @BeforeEach
    void setUp() {
        HolidayEntity holiday = HolidayEntity.builder()
                .date(LocalDate.of(2024, 1, 1))
                .name("New Year's Day")
                .localName("새해")
                .year(2024)
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .countiesKey("Seoul")
                .build();

        holiday.updateTypes(List.of(HolidayType.PUBLIC));
        holiday.updateCounties(List.of("Seoul"));

        holidayJpaRepository.save(holiday);
    }

    @Test
    @DisplayName("필터 조건에 맞는 공휴일이 조회된다. - 조건: from, to, types, countryCode")
    void findAllByFilter() {
        // given
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);
        List<HolidayType> types = List.of(HolidayType.PUBLIC);
        Boolean hasCounty = null;
        Boolean fixed = null;
        Boolean global = null;
        Integer launchYear = null;
        List<String> countryCode = List.of("KR");
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<HolidayEntity> result = holidayQueryRepository.findAllByFilter(
                from, to, types, hasCounty, fixed, global, launchYear, countryCode, pageable
        );

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        HolidayEntity holiday = result.getContent().getFirst();
        assertThat(holiday.getName()).isEqualTo("New Year's Day");
        assertThat(holiday.getCountryCode()).isEqualTo("KR");
    }

    @Test
    @DisplayName("필터 조건에 맞는 공휴일이 없으면 0을 반환한다.")
    void findAllByFilter_zero() {
        // given
        LocalDate from = LocalDate.of(2020, 12, 1);
        LocalDate to = LocalDate.of(2021, 1, 31);
        List<HolidayType> types = List.of(HolidayType.PUBLIC);
        Boolean hasCounty = null;
        Boolean fixed = null;
        Boolean global = null;
        Integer launchYear = null;
        List<String> countryCode = List.of("KR");
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<HolidayEntity> result = holidayQueryRepository.findAllByFilter(
                from, to, types, hasCounty, fixed, global, launchYear, countryCode, pageable
        );

        // then
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}

