package com.publicholidaysbycountry.holiday.presentation.request;

import com.publicholidaysbycountry.global.Constants;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRequest {

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
            return Constants.DEFAULT_SIZE;
        }
        return size;
    }

}
