package com.publicholidaysbycountry.global.exception;

import com.publicholidaysbycountry.global.response.CommonApiResponse;
import com.publicholidaysbycountry.global.response.code.ResponseCode;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CountryNotFoundException.class)
    public CommonApiResponse<?> handleCountryNotFoundException(CountryNotFoundException ex) {
        return CommonApiResponse.failure(ResponseCode.COUNTRY_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HolidayNotFoundException.class)
    public CommonApiResponse<?> handleHolidayNotFoundException(CountryNotFoundException ex) {
        return CommonApiResponse.failure(ResponseCode.HOLIDAY_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidHolidayTypeException.class)
    public CommonApiResponse<?> handleInvalidHolidayTypeException(InvalidHolidayTypeException ex) {
        return CommonApiResponse.failure(ResponseCode.INVALID_HOLIDAY_TYPE, ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public CommonApiResponse<?> handleBindException(BindException ex) {
        String message = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return CommonApiResponse.failure(ResponseCode.INVALID_REQUEST, message);
    }
}
