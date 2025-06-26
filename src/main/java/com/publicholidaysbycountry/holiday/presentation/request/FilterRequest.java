package com.publicholidaysbycountry.holiday.presentation.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class FilterRequest extends PagingRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private List<String> type;

    private Boolean hasCounty;

    private Boolean fixed;

    private Boolean global;

    @Min(value = 1000, message = "launchYear는 1000 이상이어야 합니다.")
    private Integer launchYear;

    private List<String> countryCode;

    @AssertTrue(message = "시작일(from)은 종료일(to)보다 이후일 수 없습니다.")
    public boolean isValidDateRange() {
        return from == null || to == null || !from.isAfter(to);
    }

}
