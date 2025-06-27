package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {
    Page<HolidayEntity> findByYearInAndCountryCodeIn(List<Integer> year, List<String> countryCode, Pageable pageable);

    Page<HolidayEntity> findByYearIn(List<Integer> year, Pageable pageable);

    Page<HolidayEntity> findByCountryCodeIn(List<String> countryCode, Pageable pageable);
}
