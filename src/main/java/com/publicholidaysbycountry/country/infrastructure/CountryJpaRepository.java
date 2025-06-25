package com.publicholidaysbycountry.country.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryJpaRepository extends JpaRepository<CountryEntity, Long> {
    Optional<CountryEntity> findByCode(String countryCode);
}
