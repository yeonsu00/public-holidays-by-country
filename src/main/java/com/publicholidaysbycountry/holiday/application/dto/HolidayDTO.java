package com.publicholidaysbycountry.holiday.application.dto;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record HolidayDTO(
    LocalDate date,
    String localName,
    String name,
    String countryCode,
    boolean fixed,
    boolean global,
    List<String> counties,
    Integer launchYear,
    List<HolidayType> types
) {

    public static List<Holiday> toHolidays(Set<HolidayDTO> holidayDTOs) {
        return holidayDTOs.stream()
            .map(Holiday::fromHolidayDTO)
            .toList();
    }

    public Holiday toHoliday() {
        return Holiday.builder()
            .date(date)
            .localName(localName)
            .name(name)
            .countryCode(countryCode)
            .fixed(fixed)
            .global(global)
            .counties(counties)
            .launchYear(launchYear)
            .types(types)
            .build();
    }
}
