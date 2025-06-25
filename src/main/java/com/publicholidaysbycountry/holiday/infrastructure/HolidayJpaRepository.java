package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {
}
