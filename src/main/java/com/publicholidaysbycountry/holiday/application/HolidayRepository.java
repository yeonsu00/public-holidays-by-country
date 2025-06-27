package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayRepository {
    int save(List<Holiday> holidays);

    void deleteAll();

    Page<Holiday> findByYearInAndCountryCodeIn(List<Integer> year, List<String> countryCode, Pageable pageable);

    Page<Holiday> findByYear(List<Integer> year, Pageable pageable);

    Page<Holiday> findByCountryCodeIn(List<String> countryCode, Pageable pageable);

    Page<Holiday> findAll(Pageable pageable);

    Page<Holiday> findAllByFilter(LocalDate from, LocalDate to, List<HolidayType> types, Boolean hasCounty,
                                  Boolean fixed,
                                  Boolean global, Integer launchYear, List<String> countryCode, Pageable pageable);
}
