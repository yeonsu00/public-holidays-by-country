package com.publicholidaysbycountry.holiday.presentation;

import com.publicholidaysbycountry.country.application.CountryService;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.response.CommonApiResponse;
import com.publicholidaysbycountry.holiday.application.HolidayApiService;
import com.publicholidaysbycountry.holiday.application.HolidayInitializationFacade;
import com.publicholidaysbycountry.holiday.application.HolidayService;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.presentation.request.FilterRequest;
import com.publicholidaysbycountry.holiday.presentation.request.HolidayYearAndCountryCodeRequest;
import com.publicholidaysbycountry.holiday.presentation.request.YearAndCountryFilterRequest;
import com.publicholidaysbycountry.holiday.presentation.response.HolidayResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holidays")
@Tag(name = "Holiday", description = "Holiday API")
@Slf4j
public class HolidayController {

    private final CountryService countryService;
    private final HolidayService holidayService;
    private final HolidayInitializationFacade holidayInitializationFacade;
    private final HolidayApiService holidayApiService;

    @Operation(summary = "국가별 공휴일 저장", description = "외부 API에서 가져온 국가별 공휴일을 저장합니다.")
    @PostMapping
    public CommonApiResponse<String> saveHolidaysByCountryAndYear() {
        holidayInitializationFacade.deleteAllHolidaysAndCountries();
        log.info("공휴일 데이터 초기화 완료, 국가별 공휴일 저장 시작");

        List<Country> countries = countryService.saveCountries();
        log.info("국가 데이터 저장 완료: 국가 {}개 저장", countries.size());

        List<Holiday> holidays = holidayApiService.getHolidaysFromApiByCountryAndYear(countries, LocalDate.now().getYear());
        int savedHolidayCount = holidayService.saveHolidays(holidays);
        log.info("공휴일 데이터 저장 완료: 공휴일 {}개 저장", savedHolidayCount);

        return CommonApiResponse.success(
                "최근 " + Constants.YEAR_RANGE + "년 공휴일 " + savedHolidayCount + "개가 성공적으로 저장되었습니다.");
    }

    @Operation(summary = "연도별·국가별 필터 기반 공휴일 조회", description = "연도별·국가별 필터 기반으로 공휴일을 페이징 처리하여 조회합니다.")
    @GetMapping
    public CommonApiResponse<List<HolidayResponseDTO>> getHolidaysByYearAndCountry(
            @Valid @ModelAttribute YearAndCountryFilterRequest request) {
        List<Integer> year = request.getYear();
        List<String> countryCode = request.getCountryCode();
        int page = request.getPageOrDefault();
        int size = request.getSizeOrDefault();

        boolean hasYear = hasYear(year);
        boolean hasCountry = hasCountry(countryCode);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<HolidayResponseDTO> holidayPage;

        if (hasYear && hasCountry) {
            log.info("연도와 국가 코드가 모두 제공되었습니다. 연도: {}, 국가 코드: {}", year, countryCode);
            holidayPage = holidayService.getHolidaysByYearAndCountry(year, countryCode, pageRequest);
        } else if (hasYear) {
            log.info("연도만 제공되었습니다. 연도: {}", year);
            holidayPage = holidayService.getHolidaysByYear(year, pageRequest);
        } else if (hasCountry) {
            log.info("국가 코드만 제공되었습니다. 국가 코드: {}", countryCode);
            holidayPage = holidayService.getHolidaysByCountry(countryCode, pageRequest);
        } else {
            log.info("연도와 국가 코드가 모두 제공되지 않았습니다. 전체 공휴일 조회");
            holidayPage = holidayService.getAllHolidays(pageRequest);
        }

        return getHolidaysCommonApiResponse(holidayPage);
    }

    @Operation(summary = "필터 기반 공휴일 조회", description = "date, type, county, fixed, global, launchYear, countryCode별 필터 기반으로 공휴일을 페이징 처리하여 조회합니다.")
    @GetMapping("/filter")
    public CommonApiResponse<List<HolidayResponseDTO>> getHolidaysByFilter(
            @Valid @ModelAttribute FilterRequest request) {
        log.info("공휴일 필터 조건: from={}, to={}, type={}, hasCounty={}, fixed={}, global={}, launchYear={}, countryCode={}",
                request.getFrom(), request.getTo(), request.getType(), request.getHasCounty(), request.getFixed(),
                request.getGlobal(), request.getLaunchYear(), request.getCountryCode());

        PageRequest pageRequest = PageRequest.of(request.getPageOrDefault(), request.getSizeOrDefault());

        Page<HolidayResponseDTO> holidayPage = holidayService.getHolidaysByFilter(
                request.getFrom(), request.getTo(), request.getType(), request.getHasCounty(), request.getFixed(),
                request.getGlobal(), request.getLaunchYear(), request.getCountryCode(), pageRequest
        );

        return getHolidaysCommonApiResponse(holidayPage);
    }

    @Operation(summary = "연도·국가별 공휴일 재동기화", description = "연도와 국가를 기준으로 공휴일을 재동기화합니다.")
    @PatchMapping
    public CommonApiResponse<String> refreshHolidaysByYearAndCountry(
            @Valid @RequestBody HolidayYearAndCountryCodeRequest request) {
        log.info("공휴일 재동기화 요청: 연도={}, 국가 코드={}", request.getYear(), request.getCountryCode());
        Country country = countryService.getCountryByCode(request.getCountryCode());

        List<Holiday> newHolidays = holidayApiService.getHolidays(country, request.getYear());
        int upsertedHolidayCount = holidayService.refreshHolidaysByYearAndCountry(newHolidays);

        return CommonApiResponse.success(upsertedHolidayCount + "개의 공휴일이 재동기화되었습니다.");
    }

    @Operation(summary = "연도·국가별 공휴일 삭제", description = "연도와 국가를 기준으로 공휴일을 삭제합니다.")
    @DeleteMapping
    public CommonApiResponse<String> deleteHolidaysByYearAndCountry(
            @Valid @RequestBody HolidayYearAndCountryCodeRequest request) {
        log.info("공휴일 삭제 요청: 연도={}, 국가 코드={}", request.getYear(), request.getCountryCode());
        Country country = countryService.getCountryByCode(request.getCountryCode());
        int deletedHolidayCount = holidayService.deleteHolidaysByYearAndCountry(request.getYear(), country);

        return CommonApiResponse.success(deletedHolidayCount + "개의 공휴일이 삭제되었습니다.");
    }

    private CommonApiResponse<List<HolidayResponseDTO>> getHolidaysCommonApiResponse(
            Page<HolidayResponseDTO> holidayPage) {
        return CommonApiResponse.success(
                holidayPage.getContent(),
                "총 " + holidayPage.getTotalElements() + "개 중 " + holidayPage.getNumberOfElements() + "개의 공휴일이 조회되었습니다."
        );
    }

    private boolean hasYear(List<Integer> year) {
        return year != null && !year.isEmpty();
    }

    private boolean hasCountry(List<String> countryCode) {
        return countryCode != null && !countryCode.isEmpty();
    }

}
