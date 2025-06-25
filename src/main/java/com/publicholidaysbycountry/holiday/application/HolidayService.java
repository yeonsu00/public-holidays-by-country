package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.presentation.response.HolidayResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayApiClient holidayApiClient;

    @Transactional
    public int saveHolidays(List<Country> countries, int currentYear) {
        List<Holiday> holidays = getHolidaysByCountryAndYear(countries, currentYear);
        return holidayRepository.save(holidays);
    }

    public void deleteAllHolidays() {
        holidayRepository.deleteAll();
    }

    private List<Holiday> getHolidaysByCountryAndYear(List<Country> countries, int currentYear) {
        List<HolidayDTO> holidayDTOs = new ArrayList<>();

        for (Country country : countries) {
            for (int year = currentYear; year >= currentYear - Constants.YEAR_RANGE; year--) {
                holidayDTOs.addAll(List.of(holidayApiClient.getHolidayApiRequest(country, year)));
            }
        }

        return HolidayDTO.toHolidays(holidayDTOs);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponseDTO> getHolidaysByYearAndCountry(List<Integer> year, List<String> countryCode) {
        List<Holiday> holidays = holidayRepository.findByYearAndCountryCode(year, countryCode);
        return HolidayResponseDTO.fromHolidays(holidays);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponseDTO> getHolidaysByYear(List<Integer> year) {
        List<Holiday> holidays = holidayRepository.findByYear(year);
        return HolidayResponseDTO.fromHolidays(holidays);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponseDTO> getHolidaysByCountry(List<String> countryCode) {
        List<Holiday> holidays = holidayRepository.findByCountryCodeIn(countryCode);
        return HolidayResponseDTO.fromHolidays(holidays);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponseDTO> getAllHolidays() {
        return HolidayResponseDTO.fromHolidays(holidayRepository.findAll());
    }
}
