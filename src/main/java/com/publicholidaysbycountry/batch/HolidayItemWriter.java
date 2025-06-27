package com.publicholidaysbycountry.batch;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import com.publicholidaysbycountry.holiday.infrastructure.HolidayJdbcRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayItemWriter implements ItemWriter<Holiday> {

    private final HolidayJdbcRepository holidayJdbcRepository;

    @Override
    public void write(Chunk<? extends Holiday> chunk) throws Exception {
        List<Holiday> holidays = new ArrayList<>(chunk.getItems());
        holidayJdbcRepository.upsertWithCountiesAndTypes(holidays);
    }
}
