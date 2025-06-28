package com.publicholidaysbycountry.holiday.infrastructure;

import static org.assertj.core.api.Assertions.*;

import com.publicholidaysbycountry.holiday.application.HolidayRepository;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class HolidayJdbcRepositoryTest {

    @Autowired
    private HolidayJdbcRepository holidayJdbcRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        Holiday holiday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("New Year's Day")
                .name("New Year's Day")
                .countryCode("US")
                .fixed(true)
                .global(true)
                .launchYear(null)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .build();

        holidayRepository.save(List.of(holiday));
    }

    @DisplayName("새로운 공휴일을 insert하고, 연관된 county와 type도 저장한다.")
    @Test
    void upsertWithCountiesAndTypes_insert() {
        // given
        Holiday newHoliday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("새해")
                .name("New Year's Day")
                .countryCode("KR")
                .fixed(true)
                .global(true)
                .launchYear(2000)
                .counties(List.of("Seoul", "Busan"))
                .types(List.of(HolidayType.PUBLIC, HolidayType.BANK))
                .countiesKey("Seoul,Busan")
                .build();

        // when
        int count = holidayJdbcRepository.upsertWithCountiesAndTypes(List.of(newHoliday));

        // then
        assertThat(count).isEqualTo(1);

        Integer countFromHolidayTable = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holiday WHERE name = 'New Year''s Day' AND country_code = 'KR'", Integer.class);
        assertThat(countFromHolidayTable).isEqualTo(1);

        Integer countFromCountyTable = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holiday_county", Integer.class);
        assertThat(countFromCountyTable).isEqualTo(2);

        Integer countFromTypeTable = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holiday_type", Integer.class);
        assertThat(countFromTypeTable).isEqualTo(3);
    }

    @DisplayName("기존 공휴일과 동일한 키를 가지는 경우 update되고, 해당 county와 type도 update된다.")
    @Test
    void upsertWithCountiesAndTypes_update() {
        Holiday updatedHoliday = Holiday.builder()
                .date(LocalDate.of(2025, 1, 1))
                .localName("New Year's Day")
                .name("New Year's Day")
                .countryCode("US")
                .fixed(false)
                .global(false)
                .launchYear(2000)
                .counties(null)
                .types(List.of(HolidayType.PUBLIC))
                .countiesKey("")
                .build();

        // when
        int count = holidayJdbcRepository.upsertWithCountiesAndTypes(List.of(updatedHoliday));

        // then
        assertThat(count).isEqualTo(1);

        Integer countFromHolidayTable = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holiday WHERE name = 'New Year''s Day' AND country_code = 'US'", Integer.class);
        assertThat(countFromHolidayTable).isEqualTo(1);

        Integer countFromCountyTable = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holiday_county", Integer.class);
        assertThat(countFromCountyTable).isEqualTo(0);

        Integer countFromTypeTable = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holiday_type", Integer.class);
        assertThat(countFromTypeTable).isEqualTo(1);

        Boolean fixed = jdbcTemplate.queryForObject(
                "SELECT fixed FROM holiday WHERE name = 'New Year''s Day' AND country_code = 'US'", Boolean.class);
        assertThat(fixed).isFalse();

        Boolean global = jdbcTemplate.queryForObject(
                "SELECT global FROM holiday WHERE name = 'New Year''s Day' AND country_code = 'US'", Boolean.class);
        assertThat(global).isFalse();
    }
}

