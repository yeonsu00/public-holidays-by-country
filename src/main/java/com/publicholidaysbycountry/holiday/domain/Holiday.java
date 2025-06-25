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
    private int launchYear;
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

}
