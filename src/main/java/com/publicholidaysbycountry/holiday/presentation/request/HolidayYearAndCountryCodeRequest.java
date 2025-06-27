package com.publicholidaysbycountry.holiday.presentation.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayYearAndCountryCodeRequest {

    @NotNull(message = "국가 코드는 필수입니다.")
    @NotBlank(message = "국가 코드는 공백일 수 없습니다.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "국가 코드는 대문자 2자리여야 합니다.")
    private String countryCode;

    @NotNull(message = "연도는 필수입니다.")
    @Min(value = 1000, message = "연도는 1000 이상이어야 합니다.")
    private Integer year;

}
