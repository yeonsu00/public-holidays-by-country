package com.publicholidaysbycountry.country.infrastructure;

import com.publicholidaysbycountry.country.domain.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    private CountryEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static CountryEntity fromCountry(Country country) {
        return CountryEntity.builder()
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    public Country toCountry() {
        return Country.builder()
                .code(this.code)
                .name(this.name)
                .build();
    }
}
