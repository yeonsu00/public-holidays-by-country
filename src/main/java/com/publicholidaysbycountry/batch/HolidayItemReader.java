package com.publicholidaysbycountry.batch;

import com.publicholidaysbycountry.country.application.CountryRepository;
import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.holiday.application.HolidayApiClient;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayItemReader implements ItemReader<HolidayDTO> {

    private final HolidayApiClient holidayApiClient;
    private final CountryRepository countryRepository;

    private Iterator<HolidayDTO> iterator;

    @PostConstruct
    public void init() {
        int currentYear = LocalDate.now().getYear();
        List<Country> countries = countryRepository.findAll();

        Set<HolidayDTO> holidayDTOs = new HashSet<>();
        for (Country country : countries) {
            for (int year = currentYear; year >= currentYear - 1; year--) {
                holidayDTOs.addAll(List.of(holidayApiClient.getHolidayApiRequest(country, year)));
            }
        }

        this.iterator = holidayDTOs.iterator();
    }

    @Override
    public HolidayDTO read() {
        return iterator != null && iterator.hasNext() ? iterator.next() : null;
    }
}
