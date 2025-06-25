package com.publicholidaysbycountry.country.application;

import com.publicholidaysbycountry.country.domain.Country;
import java.util.List;

public interface CountryRepository {

    List<Country> saveAll(List<Country> countries);

    void deleteAll();
}
