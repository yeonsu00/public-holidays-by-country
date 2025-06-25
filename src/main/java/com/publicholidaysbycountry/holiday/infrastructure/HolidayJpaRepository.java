package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {
    Optional<List<HolidayEntity>> findByYearInAndCountryCodeIn(List<Integer> year, List<String> countryCode);

    Optional<List<HolidayEntity>> findByYearIn(List<Integer> year);

    Optional<List<HolidayEntity>> findByCountryCodeIn(List<String> countryCode);
}
