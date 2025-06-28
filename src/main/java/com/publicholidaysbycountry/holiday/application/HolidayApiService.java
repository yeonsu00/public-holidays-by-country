package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.infrastructure.api.HolidayApiClient;
import com.publicholidaysbycountry.holiday.infrastructure.api.HolidayAsyncService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayApiService {

    private final HolidayAsyncService holidayAsyncService;
    private final HolidayApiClient holidayApiClient;

    public List<Holiday> getHolidaysFromApiByCountryAndYear(List<Country> countries, int currentYear) {
        List<CompletableFuture<List<HolidayDTO>>> futures = new ArrayList<>();

        for (Country country : countries) {
            for (int year = currentYear; year >= currentYear - Constants.YEAR_RANGE; year--) {
                futures.add(holidayAsyncService.getHolidaysAsync(country, year));
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Set<HolidayDTO> holidayDTOs = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        return HolidayDTO.toHolidays(holidayDTOs);
    }

    public List<Holiday> getHolidays(Country country, int year) {
        HolidayDTO[] response = holidayApiClient.getHolidayApiRequest(country, year);

        Set<HolidayDTO> holidayDTOs = Arrays.stream(response)
                .collect(Collectors.toSet());

        return HolidayDTO.toHolidays(holidayDTOs);
    }

}
