package com.publicholidaysbycountry.holiday.domain;

import static org.assertj.core.api.Assertions.*;

import com.publicholidaysbycountry.global.exception.InvalidHolidayTypeException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HolidayTypeTest {

    @DisplayName("유효한 HolidayType이 생성된다.")
    @Test
    void from_validTypeName() {
        // given
        String validTypeName = "Public";

        // when
        HolidayType holidayType = HolidayType.from(validTypeName);

        // then
        assertThat(holidayType).isEqualTo(HolidayType.PUBLIC);
    }

    @DisplayName("존재하지 않는 HolidayType이 생성될 때 예외가 발생한다.")
    @Test
    void from_invalidTypeName() {
        // given
        String invalidTypeName = "InvalidType";

        // when & then
        assertThatThrownBy(() -> HolidayType.from(invalidTypeName))
            .isInstanceOf(InvalidHolidayTypeException.class)
            .hasMessageContaining("에 해당하는 공휴일 타입을 찾을 수 없습니다.");

    }

    @DisplayName("대소문자를 무시하고 HolidayType이 생성된다.")
    @Test
    void from_ignoreCase() {
        // given
        String typeName = "puBlic";

        // when
        HolidayType holidayType = HolidayType.from(typeName);

        // then
        assertThat(holidayType).isEqualTo(HolidayType.PUBLIC);
    }

    @DisplayName("getTypeName()은 문자열을 반환한다.")
    @Test
    void getTypeName() {
        // when
        String typeName = HolidayType.PUBLIC.getTypeName();

        // then
        assertThat(typeName).isEqualTo("Public");
    }

    @DisplayName("유효한 타입 리스트는 HolidayType 리스트로 변환된다.")
    @Test
    void validateTypes_validList() {
        // given
        List<String> validTypes = List.of("Public", "Bank", "School");

        // when
        List<HolidayType> holidayTypes = HolidayType.validateTypes(validTypes);

        // then
        assertThat(holidayTypes)
                .containsExactly(HolidayType.PUBLIC, HolidayType.BANK, HolidayType.SCHOOL);
    }

    @DisplayName("타입 리스트로 null 입력 시 null이 반환된다.")
    @Test
    void validateTypes_nullInput() {
        // when
        List<HolidayType> holidayTypes = HolidayType.validateTypes(null);

        // then
        assertThat(holidayTypes).isNull();
    }

    @DisplayName("타입 리스트에 유효하지 않은 타입 포함 시 예외가 발생한다.")
    @Test
    void validateTypes_invalidType() {
        // given
        List<String> invalidTypes = List.of("Public", "InvalidType");

        // when & then
        assertThatThrownBy(() -> HolidayType.validateTypes(invalidTypes))
                .isInstanceOf(InvalidHolidayTypeException.class)
                .hasMessageContaining("에 해당하는 공휴일 타입을 찾을 수 없습니다.");
    }

}
