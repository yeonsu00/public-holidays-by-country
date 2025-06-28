package com.publicholidaysbycountry.initializer;

import com.publicholidaysbycountry.country.application.CountryService;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.holiday.application.HolidayApiService;
import com.publicholidaysbycountry.holiday.application.HolidayService;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class HolidayInitializer {

    private final CountryService countryService;
    private final HolidayApiService holidayApiService;
    private final HolidayService holidayService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeHolidays() {
        List<Country> countries = countryService.saveCountries();
        log.info("국가 데이터 일괄 적재 완료: 국가 {}개 저장", countries.size());

        List<Holiday> holidays = holidayApiService.getHolidaysFromApiByCountryAndYear(countries, LocalDate.now().getYear());
        int savedHolidayCount = holidayService.saveHolidays(holidays);
        log.info("공휴일 데이터 일괄 적재 완료: 공휴일 {}개 저장", savedHolidayCount);
    }
}
