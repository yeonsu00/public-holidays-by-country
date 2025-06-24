package com.publicholidaysbycountry.global.response.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    OK(HttpStatus.OK, "SUCCESS"),
    ;

    private final HttpStatus httpStatus;
    private final String code;

    ResponseCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }

}
