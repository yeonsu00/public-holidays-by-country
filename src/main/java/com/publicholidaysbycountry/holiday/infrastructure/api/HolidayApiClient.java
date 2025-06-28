package com.publicholidaysbycountry.holiday.infrastructure.api;

import com.publicholidaysbycountry.country.domain.Country;
import com.publicholidaysbycountry.global.Constants;
import com.publicholidaysbycountry.global.exception.HolidayNotFoundException;
import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HolidayApiClient {

    private final RestTemplate restTemplate;

    public HolidayDTO[] getHolidayApiRequest(Country country, int year) {
        HolidayDTO[] countryHolidayDTOs = restTemplate.getForObject(getPublicHolidaysApiUrl(year, country.getCode()),
                HolidayDTO[].class);

        if (countryHolidayDTOs == null) {
            throw new HolidayNotFoundException(year + "년도 " + country.getName() + "의 공휴일 정보를 찾을 수 없습니다.");
        }

        return countryHolidayDTOs;
    }

    private String getPublicHolidaysApiUrl(int year, String countryCode) {
        return Constants.PUBLIC_HOLIDAYS_API_URL + year + "/" + countryCode;
    }

}
