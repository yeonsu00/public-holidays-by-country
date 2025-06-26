package com.publicholidaysbycountry.holiday.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holiday_county")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HolidayCountyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countyId;

    private String countyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holidayId")
    private HolidayEntity holiday;

    @Builder
    public HolidayCountyEntity(String countyName, HolidayEntity holiday) {
        this.countyName = countyName;
        this.holiday = holiday;
    }
}
