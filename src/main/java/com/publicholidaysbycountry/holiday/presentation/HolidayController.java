package com.publicholidaysbycountry.holiday.presentation;

import com.publicholidaysbycountry.country.application.CountryService;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.response.CommonApiResponse;
import com.publicholidaysbycountry.holiday.application.HolidayInitializationFacade;
import com.publicholidaysbycountry.holiday.application.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Holiday", description = "Holiday API")
public class HolidayController {

    private final CountryService countryService;
    private final HolidayService holidayService;
    private final HolidayInitializationFacade holidayInitializationFacade;

    @Operation(summary = "국가별 공휴일 저장", description = "외부 API에서 가져온 국가별 공휴일을 저장합니다.")
    @PostMapping("/holidays")
    public CommonApiResponse<String> saveHolidaysByCountryAndYear() {
        holidayInitializationFacade.deleteAllHolidaysAndCountries();
        List<Country> countries = countryService.saveCountries();
        int savedHolidayCount = holidayService.saveHolidays(countries, LocalDate.now().getYear());
        return CommonApiResponse.success(
                "최근 " + Constants.YEAR_RANGE + "년 공휴일 " + savedHolidayCount + "개가 성공적으로 저장되었습니다.");
    }

}
