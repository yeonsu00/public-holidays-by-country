package com.publicholidaysbycountry.holiday.infrastructure.entity;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holiday")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HolidayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holidayId;

    @Column(name = "holiday_year", nullable = false)
    private Integer year;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String localName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private boolean fixed;

    @Column(nullable = false)
    private boolean global;

    private Integer launchYear;

    @Builder
    public HolidayEntity(Long holidayId, Integer year, LocalDate date, String localName, String name, String countryCode, boolean fixed,
                         boolean global, Integer launchYear) {
        this.holidayId = holidayId;
        this.year = year;
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryCode = countryCode;
        this.fixed = fixed;
        this.global = global;
        this.launchYear = launchYear;
    }

    public static HolidayEntity fromHoliday(Holiday holiday) {
        return HolidayEntity.builder()
                .year(holiday.getDate().getYear())
                .date(holiday.getDate())
                .localName(holiday.getLocalName())
                .name(holiday.getName())
                .countryCode(holiday.getCountryCode())
                .fixed(holiday.isFixed())
                .global(holiday.isGlobal())
                .launchYear(holiday.getLaunchYear())
                .build();

    }

    public Holiday toHoliday() {
        return Holiday.builder()
                .date(this.date)
                .localName(this.localName)
                .name(this.name)
                .countryCode(this.countryCode)
                .fixed(this.fixed)
                .global(this.global)
                .launchYear(this.launchYear)
                .build();
    }
}
