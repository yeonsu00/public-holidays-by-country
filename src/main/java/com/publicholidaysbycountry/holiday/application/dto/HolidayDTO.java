package com.publicholidaysbycountry.holiday.application.dto;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import java.time.LocalDate;
import java.util.List;

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

    public static List<Holiday> toHolidays(List<HolidayDTO> holidayDTOs) {
        return holidayDTOs.stream()
            .map(Holiday::fromHolidayDTO)
            .toList();
    }

}
