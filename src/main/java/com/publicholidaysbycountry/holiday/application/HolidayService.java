package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import com.publicholidaysbycountry.holiday.presentation.response.HolidayResponseDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    @Transactional
    public int saveHolidays(List<Holiday> holidays) {
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
        List<HolidayType> validatedTypes = HolidayType.validateTypes(types);

        Page<Holiday> holidays = holidayRepository.findAllByFilter(from, to, validatedTypes, hasCounty, fixed, global,
                launchYear, countryCode, pageable);
        return holidays.map(HolidayResponseDTO::fromHoliday);
    }

    @Transactional
    public int refreshHolidaysByYearAndCountry(List<Holiday> newHolidays) {
        return holidayRepository.upsertWithCountiesAndTypes(newHolidays);
    }

    @Transactional
    public int deleteHolidaysByYearAndCountry(Integer year, Country country) {
        return holidayRepository.deleteByYearAndCountryCode(year, country.getCode());
    }
}
