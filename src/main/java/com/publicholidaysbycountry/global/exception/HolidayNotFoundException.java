package com.publicholidaysbycountry.global.exception;

public class HolidayNotFoundException extends RuntimeException {

    public HolidayNotFoundException(String message) {
        super(message);
    }

}
