package com.publicholidaysbycountry.holiday.presentation.response;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import java.util.List;

public record HolidayResponseDTO(
        String date,
        String localName,
        String name,
        String countryCode,
        boolean fixed,
        boolean global,
        List<String> counties,
        Integer launchYear,
        List<HolidayType> types
) {

    public static HolidayResponseDTO fromHoliday(Holiday holiday) {
        return new HolidayResponseDTO(
                holiday.getDate().toString(),
                holiday.getLocalName(),
                holiday.getName(),
                holiday.getCountryCode(),
                holiday.isFixed(),
                holiday.isGlobal(),
                holiday.getCounties(),
                holiday.getLaunchYear(),
                holiday.getTypes()
        );
    }
}
