package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {
    List<HolidayEntity> findByYearInAndCountryCodeIn(List<Integer> year, List<String> countryCode);

    List<HolidayEntity> findByYearIn(List<Integer> year);

    List<HolidayEntity> findByCountryCodeIn(List<String> countryCode);
}
