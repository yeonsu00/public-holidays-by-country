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

    @JsonValue
    public String getTypeName() {
        return typeName;
    }

    @JsonCreator
    public static HolidayType from(String typeName) {
        for (HolidayType type : values()) {
            if (type.typeName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new InvalidHolidayTypeException(typeName + "에 해당하는 공휴일 타입을 찾을 수 없습니다.");
    }

}
