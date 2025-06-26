package com.publicholidaysbycountry.holiday.presentation.request;

import com.publicholidaysbycountry.global.Constants;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DateFilterRequest {
    @NotNull(message = "시작일(from)은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @NotNull(message = "종료일(to)은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    @Min(value = 0, message = "page는 0 이상이어야 합니다.")
    private Integer page;

    @Min(value = 1, message = "size는 1 이상이어야 합니다.")
    private Integer size;

    @AssertTrue(message = "시작일(from)은 종료일(to)보다 이후일 수 없습니다.")
    public boolean isValidDateRange() {
        return from == null || to == null || !from.isAfter(to);
    }

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
