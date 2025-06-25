package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.application.HolidayRepository;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayCountyEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayTypeEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HolidayRepositoryImpl implements HolidayRepository {

    private final HolidayJpaRepository holidayJpaRepository;
    private final HolidayCountyJpaRepository holidayCountyJpaRepository;
    private final HolidayTypeJpaRepository holidayTypeJpaRepository;

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
    public List<Holiday> findByYearAndCountryCode(List<Integer> year, List<String> countryCode) {
        List<HolidayEntity> holidayEntities = holidayJpaRepository.findByYearInAndCountryCodeIn(year, countryCode).orElseThrow();

        return holidayEntities.stream()
                .map(HolidayEntity::toHoliday)
                .toList();
    }

    @Override
    public List<Holiday> findByYear(List<Integer> year) {
        List<HolidayEntity> holidayEntities = holidayJpaRepository.findByYearIn(year).orElseThrow();

        return holidayEntities.stream()
                .map(HolidayEntity::toHoliday)
                .toList();
    }

    private HolidayEntity saveHoliday(Holiday holiday) {
        HolidayEntity holidayEntity = HolidayEntity.fromHoliday(holiday);
        return holidayJpaRepository.save(holidayEntity);
    }

    private void saveHolidayCounties(Holiday holiday, HolidayEntity savedHolidayEntity) {
        List<HolidayCountyEntity> countyEntities = holiday.getCounties().stream()
                .map(countyName -> new HolidayCountyEntity(countyName, savedHolidayEntity.getHolidayId()))
                .toList();
        holidayCountyJpaRepository.saveAll(countyEntities);
    }

    private void saveHolidayTypes(Holiday holiday, HolidayEntity savedHolidayEntity) {
        List<HolidayTypeEntity> holidayTypeEntities = holiday.getTypes().stream()
                .map(type -> new HolidayTypeEntity(savedHolidayEntity.getHolidayId(), type))
                .toList();
        holidayTypeJpaRepository.saveAll(holidayTypeEntities);
    }
}
