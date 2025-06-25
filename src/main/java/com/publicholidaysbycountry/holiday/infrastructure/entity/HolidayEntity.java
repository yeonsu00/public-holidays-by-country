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

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String localName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long countryId;

    @Column(nullable = false)
    private boolean fixed;

    @Column(nullable = false)
    private boolean global;

    private Integer launchYear;

    @Builder
    public HolidayEntity(Long holidayId, LocalDate date, String localName, String name, Long countryId, boolean fixed,
                         boolean global, Integer launchYear) {
        this.holidayId = holidayId;
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.countryId = countryId;
        this.fixed = fixed;
        this.global = global;
        this.launchYear = launchYear;
    }

    public static HolidayEntity fromHoliday(Holiday holiday, Long countryId) {
        return HolidayEntity.builder()
                .date(holiday.getDate())
                .localName(holiday.getLocalName())
                .name(holiday.getName())
                .countryId(countryId)
                .fixed(holiday.isFixed())
                .global(holiday.isGlobal())
                .launchYear(holiday.getLaunchYear())
                .build();

    }
}
