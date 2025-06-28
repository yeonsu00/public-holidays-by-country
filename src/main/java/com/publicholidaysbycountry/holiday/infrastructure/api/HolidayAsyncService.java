package com.publicholidaysbycountry.holiday.infrastructure.api;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayAsyncService {

    private final HolidayApiClient holidayApiClient;

    @Async
    public CompletableFuture<List<HolidayDTO>> getHolidaysAsync(Country country, int year) {
        HolidayDTO[] response = holidayApiClient.getHolidayApiRequest(country, year);
        return CompletableFuture.completedFuture(Arrays.asList(response));
    }
}
