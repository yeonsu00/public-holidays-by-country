package com.publicholidaysbycountry.holiday.presentation;

import com.publicholidaysbycountry.country.application.CountryService;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.response.CommonApiResponse;
import com.publicholidaysbycountry.holiday.application.HolidayInitializationFacade;
import com.publicholidaysbycountry.holiday.application.HolidayService;
import com.publicholidaysbycountry.holiday.presentation.response.HolidayResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holidays")
@Tag(name = "Holiday", description = "Holiday API")
public class HolidayController {

    private final CountryService countryService;
    private final HolidayService holidayService;
    private final HolidayInitializationFacade holidayInitializationFacade;

    @Operation(summary = "국가별 공휴일 저장", description = "외부 API에서 가져온 국가별 공휴일을 저장합니다.")
    @PostMapping
    public CommonApiResponse<String> saveHolidaysByCountryAndYear() {
        holidayInitializationFacade.deleteAllHolidaysAndCountries();
        List<Country> countries = countryService.saveCountries();
        int savedHolidayCount = holidayService.saveHolidays(countries, LocalDate.now().getYear());
        return CommonApiResponse.success(
                "최근 " + Constants.YEAR_RANGE + "년 공휴일 " + savedHolidayCount + "개가 성공적으로 저장되었습니다.");
    }

    @Operation(summary = "필터 기반 공휴일 조회", description = "연도별·국가별 필터 기반으로 공휴일을 조회합니다.")
    @GetMapping
    public CommonApiResponse<List<HolidayResponseDTO>> getHolidaysByFilter(@RequestParam(required = false) List<Integer> year,
                                                        @RequestParam(required = false) List<String> countryCode) {
        boolean hasYear = hasYear(year);
        boolean hasCountry = HasCountry(countryCode);

        List<HolidayResponseDTO> holidayResponseDTOs;

        if (hasYear && hasCountry) {
            holidayResponseDTOs = holidayService.getHolidaysByYearAndCountry(year, countryCode);
        } else if (hasYear) {
            holidayResponseDTOs = holidayService.getHolidaysByYear(year);
        } else if (hasCountry) {
            holidayResponseDTOs = holidayService.getHolidaysByCountry(countryCode);
        } else {
            holidayResponseDTOs = holidayService.getAllHolidays();
        }

        return CommonApiResponse.success(holidayResponseDTOs, "공휴일 " + holidayResponseDTOs.size() + "개가 조회되었습니다.");
    }

    private boolean hasYear(List<Integer> year) {
        return year != null && !year.isEmpty();
    }

    private boolean HasCountry(List<String> countryCode) {
        return countryCode != null && !countryCode.isEmpty();
    }

}
