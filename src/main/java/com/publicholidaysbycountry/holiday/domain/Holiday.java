package com.publicholidaysbycountry.holiday.domain;

import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Holiday {
    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean fixed;
    private boolean global;
    private List<String> counties;
    private Integer launchYear;
    private List<HolidayType> types;
    private String countiesKey;

    @Builder
    public Holiday(LocalDate date, String localName, String name, String countryCode, boolean fixed, boolean global,
                   List<String> counties, Integer launchYear, List<HolidayType> types, String countiesKey) {
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryCode = countryCode;
        this.fixed = fixed;
        this.global = global;
        this.counties = counties;
        this.launchYear = launchYear;
        this.types = types;
        this.countiesKey = countiesKey;
    }

    public static Holiday fromHolidayDTO(HolidayDTO holidayDTO) {
        return Holiday.builder()
                .date(holidayDTO.date())
                .localName(holidayDTO.localName())
                .name(holidayDTO.name())
                .countryCode(holidayDTO.countryCode())
                .fixed(holidayDTO.fixed())
                .global(holidayDTO.global())
                .counties(holidayDTO.counties())
                .launchYear(holidayDTO.launchYear())
                .types(holidayDTO.types())
                .countiesKey(createCountiesKey(holidayDTO.counties()))
                .build();
    }

    public boolean hasCounties() {
        return this.counties != null && !this.counties.isEmpty();
    }

    public boolean hasTypes() {
        return this.types != null && !this.types.isEmpty();
    }

    private static String createCountiesKey(List<String> counties) {
        if (counties == null || counties.isEmpty()) {
            return "";
        }

        return counties.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

}
