package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.domain.Holiday;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HolidayJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public int upsertWithCountiesAndTypes(List<Holiday> newHolidays) {
        int upsertedHolidayCount = bulkUpsert(newHolidays);

        for (Holiday holiday : newHolidays) {
            Long holidayId = findHolidayId(holiday);

            if (holiday.getCounties() != null && !holiday.getCounties().isEmpty()) {
                updateCounties(holidayId, holiday.getCounties());
            }

            if (holiday.getTypes() != null && !holiday.getTypes().isEmpty()) {
                List<String> typeNames = holiday.getTypes().stream()
                        .map(Enum::name)
                        .toList();
                updateTypes(holidayId, typeNames);
            }

        }

        return upsertedHolidayCount;
    }

    public int bulkUpsert(List<Holiday> holidays) {
        String sql = """
                MERGE INTO holiday (
                    name, local_name, holiday_year, country_code, date,
                    fixed, global, launch_year, counties_key
                )
                KEY (name, local_name, holiday_year, date, country_code, counties_key)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        List<Object[]> params = holidays.stream()
                .map(h -> new Object[]{
                        h.getName(),
                        h.getLocalName(),
                        h.getDate().getYear(),
                        h.getCountryCode(),
                        h.getDate(),
                        h.isFixed(),
                        h.isGlobal(),
                        h.getLaunchYear(),
                        h.getCountiesKey()
                })
                .toList();

        int[] results = jdbcTemplate.batchUpdate(sql, params);
        return results.length;
    }

    private Long findHolidayId(Holiday holiday) {
        String sql = """
                SELECT holiday_id FROM holiday
                WHERE name = ? AND local_name = ? AND holiday_year = ? AND date = ? AND country_code = ? AND counties_key = ?
                """;
        Object[] params = new Object[]{
                holiday.getName(),
                holiday.getLocalName(),
                holiday.getDate().getYear(),
                holiday.getDate(),
                holiday.getCountryCode(),
                holiday.getCountiesKey()
        };

        return jdbcTemplate.queryForObject(sql, Long.class, params);
    }

    private void updateCounties(Long holidayId, List<String> newCounties) {
        List<String> existingCountyName = findExistingCounties(holidayId);

        List<String> toAddCounty = newCounties.stream()
                .filter(c -> !existingCountyName.contains(c))
                .toList();

        List<String> toRemoveCounty = existingCountyName.stream()
                .filter(c -> !newCounties.contains(c))
                .toList();

        if (!toAddCounty.isEmpty()) {
            insertCounties(holidayId, toAddCounty);
        }
        if (!toRemoveCounty.isEmpty()) {
            deleteCounties(holidayId, toRemoveCounty);
        }
    }

    private List<String> findExistingCounties(Long holidayId) {
        String sql = "SELECT county_name FROM holiday_county WHERE holiday_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, holidayId);
    }

    private void deleteCounties(Long holidayId, List<String> counties) {
        String sql = "DELETE FROM holiday_county WHERE holiday_id = ? AND county_name = ?";
        List<Object[]> params = counties.stream()
                .map(county -> new Object[]{holidayId, county})
                .toList();
        jdbcTemplate.batchUpdate(sql, params);
    }

    private void insertCounties(Long holidayId, List<String> counties) {
        String sql = """
                INSERT INTO holiday_county (holiday_id, county_name)
                VALUES (?, ?)
                """;

        List<Object[]> params = counties.stream()
                .map(county -> new Object[]{holidayId, county})
                .toList();

        jdbcTemplate.batchUpdate(sql, params);
    }

    private void updateTypes(Long holidayId, List<String> newTypes) {
        List<String> existingTypes = findExistingTypes(holidayId);

        List<String> toAddTypes = newTypes.stream()
                .filter(t -> !existingTypes.contains(t))
                .toList();

        List<String> toRemoveTypes = existingTypes.stream()
                .filter(t -> !newTypes.contains(t))
                .toList();

        if (!toAddTypes.isEmpty()) {
            insertTypes(holidayId, toAddTypes);
        }
        if (!toRemoveTypes.isEmpty()) {
            deleteTypes(holidayId, toRemoveTypes);
        }
    }

    private List<String> findExistingTypes(Long holidayId) {
        String sql = "SELECT type FROM holiday_type WHERE holiday_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, holidayId);
    }

    private void deleteTypes(Long holidayId, List<String> types) {
        String sql = "DELETE FROM holiday_type WHERE holiday_id = ? AND type = ?";
        List<Object[]> params = types.stream()
                .map(type -> new Object[]{holidayId, type})
                .toList();
        jdbcTemplate.batchUpdate(sql, params);
    }

    private void insertTypes(Long holidayId, List<String> types) {
        String sql = """
                INSERT INTO holiday_type (holiday_id, type)
                VALUES (?, ?)
                """;

        List<Object[]> params = types.stream()
                .map(type -> new Object[]{holidayId, type})
                .toList();

        jdbcTemplate.batchUpdate(sql, params);
    }

}
