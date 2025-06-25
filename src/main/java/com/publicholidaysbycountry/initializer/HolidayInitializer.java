package com.publicholidaysbycountry.initializer;

import com.publicholidaysbycountry.country.application.CountryService;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.holiday.application.HolidayService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayInitializer {

    private final CountryService countryService;
    private final HolidayService holidayService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeHolidays() {
        List<Country> countries = countryService.saveCountries();
        holidayService.saveHolidays(countries, LocalDate.now().getYear());
    }
}
