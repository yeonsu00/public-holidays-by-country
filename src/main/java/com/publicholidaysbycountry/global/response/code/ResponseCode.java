package com.publicholidaysbycountry.global.response.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    OK(HttpStatus.OK, "SUCCESS"),

    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "COUNTRY_NOT_FOUND"),
    HOLIDAY_NOT_FOUND(HttpStatus.NOT_FOUND, "HOLIDAY_NOT_FOUND"),
    INVALID_HOLIDAY_TYPE(HttpStatus.NOT_FOUND, "INVALID_HOLIDAY_TYPE"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST"),
    ;

    private final HttpStatus httpStatus;
    private final String code;

    ResponseCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }

}
