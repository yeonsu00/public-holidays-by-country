package com.publicholidaysbycountry.global.exception;

import com.publicholidaysbycountry.global.response.CommonApiResponse;
import com.publicholidaysbycountry.global.response.code.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CountryNotFoundException.class)
    public CommonApiResponse<?> handleCountryNotFoundException(CountryNotFoundException ex) {
        return CommonApiResponse.failure(ResponseCode.COUNTRY_NOT_FOUND, ex.getMessage());
    }

}
