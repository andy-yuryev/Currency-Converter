package com.example.currencyconverter.repository;

import com.example.currencyconverter.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select count(distinct r.date) > 0 from Rate r where r.date = :date")
    boolean findRateDate(@Param("date") LocalDate date);

    Rate findByCurrencyIdAndDate(String currencyId, LocalDate date);
}
