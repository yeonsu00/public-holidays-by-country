package com.publicholidaysbycountry.holiday.infrastructure.entity;

import com.publicholidaysbycountry.holiday.domain.HolidayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "holiday_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HolidayTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holidayTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holidayId")
    private HolidayEntity holiday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayType type;

    @Builder
    public HolidayTypeEntity(HolidayEntity holiday, HolidayType type) {
        this.holiday = holiday;
        this.type = type;
    }
}
