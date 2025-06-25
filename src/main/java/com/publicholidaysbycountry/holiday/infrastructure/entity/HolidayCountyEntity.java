package com.publicholidaysbycountry.holiday.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holiday_county")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayCountyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countyId;

    private String countyName;

    private Long holidayId;

    @Builder
    public HolidayCountyEntity(String countyName, Long holidayId) {
        this.countyName = countyName;
        this.holidayId = holidayId;
    }
}
