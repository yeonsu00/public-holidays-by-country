package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayRepository {
    int save(List<Holiday> holidays);

    void deleteAll();

    Page<Holiday> findByYearAndCountryCode(List<Integer> year, List<String> countryCode, Pageable pageable);

    Page<Holiday> findByYear(List<Integer> year, Pageable pageable);

    Page<Holiday> findByCountryCodeIn(List<String> countryCode, Pageable pageable);

    Page<Holiday> findAll(Pageable pageable);
}
