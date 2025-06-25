package com.publicholidaysbycountry.holiday.domain;

import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import java.time.LocalDate;
import java.util.List;
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

    @Builder
    public Holiday(LocalDate date, String localName, String name, String countryCode, boolean fixed, boolean global,
                   List<String> counties, int launchYear, List<HolidayType> types) {
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryCode = countryCode;
        this.fixed = fixed;
        this.global = global;
        this.counties = counties;
        this.launchYear = launchYear;
        this.types = types;
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
                .build();
    }

    public boolean hasCounties() {
        return this.counties != null && !this.counties.isEmpty();
    }

    public boolean hasTypes() {
        return this.types != null && !this.types.isEmpty();
    }

}
