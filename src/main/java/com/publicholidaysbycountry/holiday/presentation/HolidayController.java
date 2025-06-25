package com.publicholidaysbycountry.holiday.presentation;

import com.publicholidaysbycountry.country.application.CountryService;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.response.CommonApiResponse;
import com.publicholidaysbycountry.holiday.application.HolidayService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HolidayController {

    private final CountryService countryService;
    private final HolidayService holidayService;

    @PostMapping("/holidays")
    public CommonApiResponse<String> saveHolidaysByCountryAndYear() {
        List<Country> countries = countryService.saveCountries();
        holidayService.saveHolidays(countries, LocalDate.now().getYear());
        return CommonApiResponse.success("최근 "+ Constants.YEAR_RANGE + "년 공휴일이 성공적으로 저장되었습니다.");
    }

}
