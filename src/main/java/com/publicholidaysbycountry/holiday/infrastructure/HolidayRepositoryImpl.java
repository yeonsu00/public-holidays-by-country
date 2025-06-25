package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.country.infrastructure.CountryEntity;
import com.publicholidaysbycountry.country.infrastructure.CountryJpaRepository;
import com.publicholidaysbycountry.global.exception.CountryNotFoundException;
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
    private final CountryJpaRepository countryJpaRepository;

    @Override
    public void save(List<Holiday> holidays) {
        for (Holiday holiday : holidays) {
            HolidayEntity savedHolidayEntity = saveHoliday(holiday);

            if (holiday.hasCounties()) {
                saveHolidayCounties(holiday, savedHolidayEntity);
            }

            if (holiday.hasTypes()) {
                saveHolidayTypes(holiday, savedHolidayEntity);
            }
        }
    }

    @Override
    public void deleteAll() {
        holidayTypeJpaRepository.deleteAllInBatch();
        holidayCountyJpaRepository.deleteAllInBatch();
        holidayJpaRepository.deleteAllInBatch();
    }

    private HolidayEntity saveHoliday(Holiday holiday) {
        Long countryId = getCountryIdByHolidayCountry(holiday);

        HolidayEntity holidayEntity = HolidayEntity.fromHoliday(holiday, countryId);
        return holidayJpaRepository.save(holidayEntity);
    }

    private Long getCountryIdByHolidayCountry(Holiday holiday) {
        CountryEntity countryEntity = countryJpaRepository.findByCode(holiday.getCountryCode())
                .orElseThrow(() -> new CountryNotFoundException(
                        "국가 코드 " + holiday.getCountryCode() + "에 해당하는 국가의 정보를 찾을 수 없습니다."));

        return countryEntity.getCountryId();
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
