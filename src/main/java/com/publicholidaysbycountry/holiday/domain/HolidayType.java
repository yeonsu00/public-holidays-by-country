package com.publicholidaysbycountry.holiday.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.publicholidaysbycountry.global.exception.InvalidHolidayTypeException;

public enum HolidayType {

    PUBLIC("Public"),
    BANK("Bank"),
    SCHOOL("School"),
    AUTHORITIES("Authorities"),
    OPTIONAL("Optional"),
    OBSERVANCE("Observance"),
    OTHER("Other");

    private final String typeName;

    HolidayType(String typeName) {
        this.typeName = typeName;
    }

}
