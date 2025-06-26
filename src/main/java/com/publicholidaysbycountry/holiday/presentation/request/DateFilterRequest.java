package com.publicholidaysbycountry.holiday.presentation.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class DateFilterRequest extends PagingRequest {

    @NotNull(message = "시작일(from)은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @NotNull(message = "종료일(to)은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    @AssertTrue(message = "시작일(from)은 종료일(to)보다 이후일 수 없습니다.")
    public boolean isValidDateRange() {
        return from == null || to == null || !from.isAfter(to);
    }

}
