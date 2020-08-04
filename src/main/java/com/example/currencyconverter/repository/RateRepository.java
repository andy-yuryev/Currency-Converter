package com.example.currencyconverter.repository;

import com.example.currencyconverter.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select max(r.date) from Rate r")
    Date findLastRateDate();

    Rate findByCurrencyIdAndDate(String currencyId, Date date);
}
