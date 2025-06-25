package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayTypeJpaRepository extends JpaRepository<HolidayTypeEntity, Long> {
}
