package com.publicholidaysbycountry.holiday.presentation.request;

import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YearAndCountryFilterRequest extends PagingRequest {

    private List<@Positive(message = "year는 양수여야 합니다.") Integer> year;

    private List<String> countryCode;

}
