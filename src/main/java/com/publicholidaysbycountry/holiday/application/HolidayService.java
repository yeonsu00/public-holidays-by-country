package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.exception.HolidayNotFoundException;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public void saveHolidays(List<Country> countries, int currentYear) {
        List<Holiday> holidays = getApiRequest(countries, currentYear);
        holidayRepository.save(holidays);
    }

    public void deleteAllHolidays() {
        holidayRepository.deleteAll();
    }

    private List<Holiday> getApiRequest(List<Country> countries, int currentYear) {
        List<HolidayDTO> holidayDTOs = new ArrayList<>();

        for (Country country : countries) {
            for (int year = currentYear; year >= currentYear - Constants.YEAR_RANGE; year--) {
                holidayDTOs.addAll(getHolidayDTOsByCountryAndYear(country, year));
            }
        }

        return HolidayDTO.toHolidays(holidayDTOs);
    }

    private List<HolidayDTO> getHolidayDTOsByCountryAndYear(Country country, int year) {
        HolidayDTO[] countryHolidayDTOs = restTemplate.getForObject(getPublicHolidaysApiUrl(year, country.getCode()),
                HolidayDTO[].class);

        if (countryHolidayDTOs == null) {
            throw new HolidayNotFoundException(year + "년도 " + country.getName() + "의 공휴일 정보를 찾을 수 없습니다.");
        }

        return List.of(countryHolidayDTOs);
    }

    private static String getPublicHolidaysApiUrl(int year, String countryCode) {
        return Constants.PUBLIC_HOLIDAYS_API_URL + year + "/" + countryCode;
    }

}
