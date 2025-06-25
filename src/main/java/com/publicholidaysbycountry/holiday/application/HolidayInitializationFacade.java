package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.application.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
