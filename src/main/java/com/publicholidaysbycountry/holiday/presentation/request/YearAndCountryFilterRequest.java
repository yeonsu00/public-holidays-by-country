package com.publicholidaysbycountry.holiday.presentation.request;

import com.publicholidaysbycountry.global.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Data;

@Data
public class YearAndCountryFilterRequest {
    private List<@Positive(message = "year는 양수여야 합니다.") Integer> year;
    private List<String> countryCode;

    @Min(value = 0, message = "page는 0 이상이어야 합니다.")
    private Integer page;

    @Min(value = 1, message = "size는 1 이상이어야 합니다.")
    private Integer size;

    public int getPageOrDefault() {
        if (page == null || page < 0) {
            return Constants.DEFAULT_PAGE;
        }
        return page;
    }

    public int getSizeOrDefault() {
        if (size == null || size < 1) {
            return 10;
        }
        return Constants.DEFAULT_SIZE;
    }
}
