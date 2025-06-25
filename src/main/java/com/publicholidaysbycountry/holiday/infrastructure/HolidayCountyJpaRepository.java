package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayCountyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayCountyJpaRepository extends JpaRepository<HolidayCountyEntity, Long> {
}
