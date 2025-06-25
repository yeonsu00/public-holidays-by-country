package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.application.CountryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayInitializationFacade {

    private final HolidayService holidayService;
    private final CountryService countryService;

    @Transactional
    public void deleteAllHolidaysAndCountries() {
        holidayService.deleteAllHolidays();
        countryService.deleteAllCountries();
    }
}
