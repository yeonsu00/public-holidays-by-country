package com.publicholidaysbycountry.batch;

import com.publicholidaysbycountry.holiday.application.dto.HolidayDTO;
import com.publicholidaysbycountry.holiday.domain.Holiday;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class HolidayItemProcessor implements ItemProcessor<HolidayDTO, Holiday> {
    @Override
    public Holiday process(HolidayDTO item) {
        return item.toHoliday();
    }
}
