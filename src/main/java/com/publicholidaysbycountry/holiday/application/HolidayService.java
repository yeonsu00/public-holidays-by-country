package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.exception.InvalidHolidayTypeException;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import com.publicholidaysbycountry.holiday.infrastructure.HolidayJdbcRepository;
import com.publicholidaysbycountry.holiday.presentation.response.HolidayResponseDTO;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayApiClient holidayApiClient;
    private final HolidayJdbcRepository holidayJdbcRepository;

    @Transactional
    public int saveHolidays(List<Country> countries, int currentYear) {
        List<Holiday> holidays = getHolidaysByCountryAndYear(countries, currentYear);
        return holidayRepository.save(holidays);
    }

    public void deleteAllHolidays() {
        holidayRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Page<HolidayResponseDTO> getHolidaysByYearAndCountry(List<Integer> year, List<String> countryCode,
                                                                Pageable pageable) {
        Page<Holiday> holidays = holidayRepository.findByYearInAndCountryCodeIn(year, countryCode, pageable);
        return holidays.map(HolidayResponseDTO::fromHoliday);
    }

    @Transactional(readOnly = true)
    public Page<HolidayResponseDTO> getHolidaysByYear(List<Integer> year, Pageable pageable) {
        Page<Holiday> holidays = holidayRepository.findByYear(year, pageable);
        return holidays.map(HolidayResponseDTO::fromHoliday);
    }

    @Transactional(readOnly = true)
    public Page<HolidayResponseDTO> getHolidaysByCountry(List<String> countryCode, Pageable pageable) {
        Page<Holiday> holidays = holidayRepository.findByCountryCodeIn(countryCode, pageable);
        return holidays.map(HolidayResponseDTO::fromHoliday);
    }

    @Transactional(readOnly = true)
    public Page<HolidayResponseDTO> getAllHolidays(Pageable pageable) {
        return holidayRepository.findAll(pageable)
                .map(HolidayResponseDTO::fromHoliday);
    }

    @Transactional(readOnly = true)
    public Page<HolidayResponseDTO> getHolidaysByFilter(LocalDate from, LocalDate to, List<String> types,
                                                        Boolean hasCounty, Boolean fixed, Boolean global,
                                                        Integer launchYear, List<String> countryCode,
                                                        Pageable pageable) {
        List<HolidayType> validatedTypes = validateTypes(types);

        Page<Holiday> holidays = holidayRepository.findAllByFilter(from, to, validatedTypes, hasCounty, fixed, global,
                launchYear, countryCode, pageable);
        return holidays.map(HolidayResponseDTO::fromHoliday);
    }

    @Transactional
    public int refreshHolidaysByYearAndCountry(Integer year, Country country) {
        Set<HolidayDTO> holidayDTOs = new HashSet<>(
                Arrays.asList(holidayApiClient.getHolidayApiRequest(country, year)));
        List<Holiday> newHolidays = HolidayDTO.toHolidays(holidayDTOs);

        return holidayJdbcRepository.upsertWithCountiesAndTypes(newHolidays);
    }

    private List<Holiday> getHolidaysByCountryAndYear(List<Country> countries, int currentYear) {
        Set<HolidayDTO> holidayDTOs = new HashSet<>();

        for (Country country : countries) {
            for (int year = currentYear; year >= currentYear - Constants.YEAR_RANGE; year--) {
                holidayDTOs.addAll(List.of(holidayApiClient.getHolidayApiRequest(country, year)));
            }
        }

        return HolidayDTO.toHolidays(holidayDTOs);
    }

    private List<HolidayType> validateTypes(List<String> types) {
        if (types == null) {
            return null;
        }

        try {
            return types.stream()
                    .map(String::toUpperCase)
                    .map(HolidayType::valueOf)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidHolidayTypeException("지원하지 않는 HolidayType이 포함되어 있습니다: " + types);
        }
    }
}
