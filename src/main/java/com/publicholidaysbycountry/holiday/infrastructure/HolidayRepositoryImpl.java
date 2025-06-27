package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.application.HolidayRepository;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.domain.HolidayType;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayCountyEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayTypeEntity;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HolidayRepositoryImpl implements HolidayRepository {

    private final HolidayJpaRepository holidayJpaRepository;
    private final HolidayCountyJpaRepository holidayCountyJpaRepository;
    private final HolidayTypeJpaRepository holidayTypeJpaRepository;
    private final HolidayQueryRepository holidayQueryRepository;
    private final HolidayJdbcRepository holidayJdbcRepository;

    @Override
    public int save(List<Holiday> holidays) {
        int savedHolidayCount = 0;

        for (Holiday holiday : holidays) {
            HolidayEntity savedHolidayEntity = saveHoliday(holiday);
            savedHolidayCount++;

            if (holiday.hasCounties()) {
                saveHolidayCounties(holiday, savedHolidayEntity);
            }

            if (holiday.hasTypes()) {
                saveHolidayTypes(holiday, savedHolidayEntity);
            }
        }

        return savedHolidayCount;
    }

    @Override
    public void deleteAll() {
        holidayTypeJpaRepository.deleteAllInBatch();
        holidayCountyJpaRepository.deleteAllInBatch();
        holidayJpaRepository.deleteAllInBatch();
    }

    @Override
    public Page<Holiday> findByYearInAndCountryCodeIn(List<Integer> year, List<String> countryCode, Pageable pageable) {
        Page<HolidayEntity> holidayEntities = holidayJpaRepository.findByYearInAndCountryCodeIn(year, countryCode,
                pageable);
        return holidayEntities.map(HolidayEntity::toHoliday);
    }

    @Override
    public Page<Holiday> findByYear(List<Integer> year, Pageable pageable) {
        Page<HolidayEntity> holidayEntities = holidayJpaRepository.findByYearIn(year, pageable);
        return holidayEntities.map(HolidayEntity::toHoliday);
    }

    @Override
    public Page<Holiday> findByCountryCodeIn(List<String> countryCode, Pageable pageable) {
        Page<HolidayEntity> holidayEntities = holidayJpaRepository.findByCountryCodeIn(countryCode, pageable);
        return holidayEntities.map(HolidayEntity::toHoliday);
    }

    @Override
    public Page<Holiday> findAll(Pageable pageable) {
        Page<HolidayEntity> holidayEntities = holidayJpaRepository.findAll(pageable);
        return holidayEntities.map(HolidayEntity::toHoliday);
    }

    @Override
    public Page<Holiday> findAllByFilter(LocalDate from, LocalDate to, List<HolidayType> types, Boolean hasCounty,
                                         Boolean fixed, Boolean global, Integer launchYear, List<String> countryCode,
                                         Pageable pageable) {
        Page<HolidayEntity> holidayEntities = holidayQueryRepository.findAllByFilter(from, to, types, hasCounty, fixed,
                global, launchYear, countryCode, pageable);
        return holidayEntities.map(HolidayEntity::toHoliday);
    }

    @Override
    public int upsertWithCountiesAndTypes(List<Holiday> newHolidays) {
        return holidayJdbcRepository.upsertWithCountiesAndTypes(newHolidays);
    }

    @Override
    public int deleteByYearAndCountryCode(Integer year, String code) {
        return holidayJpaRepository.deleteByYearAndCountryCode(year, code);
    }

    private HolidayEntity saveHoliday(Holiday holiday) {
        HolidayEntity holidayEntity = HolidayEntity.fromHoliday(holiday);
        return holidayJpaRepository.save(holidayEntity);
    }

    private void saveHolidayCounties(Holiday holiday, HolidayEntity savedHolidayEntity) {
        List<HolidayCountyEntity> countyEntities = holiday.getCounties().stream()
                .map(countyName -> new HolidayCountyEntity(countyName, savedHolidayEntity))
                .toList();
        holidayCountyJpaRepository.saveAll(countyEntities);
    }

    private void saveHolidayTypes(Holiday holiday, HolidayEntity savedHolidayEntity) {
        List<HolidayTypeEntity> holidayTypeEntities = holiday.getTypes().stream()
                .map(type -> new HolidayTypeEntity(savedHolidayEntity, type))
                .toList();
        holidayTypeJpaRepository.saveAll(holidayTypeEntities);
    }
}
