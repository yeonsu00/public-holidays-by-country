package com.publicholidaysbycountry.holiday.infrastructure;

import com.publicholidaysbycountry.holiday.domain.HolidayType;
import com.publicholidaysbycountry.holiday.infrastructure.entity.HolidayEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.QHolidayCountyEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.QHolidayEntity;
import com.publicholidaysbycountry.holiday.infrastructure.entity.QHolidayTypeEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HolidayQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<HolidayEntity> findAllByFilter(LocalDate from, LocalDate to, List<HolidayType> types, Boolean hasCounty,
                                               Boolean fixed, Boolean global, Integer launchYear,
                                               List<String> countryCode, Pageable pageable) {
        QHolidayEntity qHoliday = QHolidayEntity.holidayEntity;
        QHolidayTypeEntity qType = QHolidayTypeEntity.holidayTypeEntity;
        QHolidayCountyEntity qCounty = QHolidayCountyEntity.holidayCountyEntity;

        JPQLQuery<HolidayEntity> query = jpaQueryFactory
                .selectFrom(qHoliday)
                .distinct()
                .leftJoin(qHoliday.types, qType)
                .leftJoin(qHoliday.counties, qCounty);

        BooleanBuilder builder = getBooleanBuilderByFilter(from, to, types, hasCounty, fixed, global, launchYear,
                countryCode, qHoliday, qType, qCounty);

        query.where(builder);

        long total = query.fetch().size();

        List<HolidayEntity> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanBuilder getBooleanBuilderByFilter(LocalDate from, LocalDate to, List<HolidayType> types,
                                                     Boolean hasCounty, Boolean fixed, Boolean global,
                                                     Integer launchYear, List<String> countryCode,
                                                     QHolidayEntity qHoliday, QHolidayTypeEntity qType,
                                                     QHolidayCountyEntity qCounty) {
        BooleanBuilder builder = new BooleanBuilder();

        if (from != null) {
            builder.and(qHoliday.date.goe(from));
        }
        if (to != null) {
            builder.and(qHoliday.date.loe(to));
        }
        if (types != null && !types.isEmpty()) {
            builder.and(qHoliday.holidayId.in(
                    JPAExpressions.select(qType.holiday.holidayId)
                            .from(qType)
                            .where(qType.type.in(types))
            ));
        }
        if (fixed != null) {
            builder.and(qHoliday.fixed.eq(fixed));
        }
        if (global != null) {
            builder.and(qHoliday.global.eq(global));
        }
        if (launchYear != null) {
            builder.and(qHoliday.launchYear.eq(launchYear));
        }
        if (hasCounty != null) {
            if (hasCounty) {
                builder.and(qCounty.countyId.isNotNull());
            } else {
                builder.and(qCounty.countyId.isNull());
            }
        }
        if (countryCode != null && !countryCode.isEmpty()) {
            builder.and(qHoliday.countryCode.in(countryCode));
        }
        return builder;
    }
}
