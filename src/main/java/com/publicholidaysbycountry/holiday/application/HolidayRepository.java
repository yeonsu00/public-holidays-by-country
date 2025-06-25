package com.publicholidaysbycountry.holiday.application;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import java.util.List;

public interface HolidayRepository {
    void save(List<Holiday> holidays);

    void deleteAll();
}
