package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.application.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class HolidayInitializationFacadeTest {

    @Mock
    private HolidayService holidayService;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private HolidayInitializationFacade holidayInitializationFacade;

    @Test
    @DisplayName("deleteAllHolidaysAndCountries 호출 시 Holiday와 Country 삭제 메서드가 실행된다.")
    void deleteAllHolidaysAndCountries_success() {
        // when
        holidayInitializationFacade.deleteAllHolidaysAndCountries();

        // then
        verify(holidayService).deleteAllHolidays();
        verify(countryService).deleteAllCountries();
    }
}

