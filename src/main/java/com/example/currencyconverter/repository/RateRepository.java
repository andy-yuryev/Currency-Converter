package com.example.currencyconverter.repository;

import com.example.currencyconverter.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select count(distinct r.date) > 0 from Rate r where r.date = :date")
    boolean findRateDate(@Param("date") Date date);

    Rate findByCurrencyIdAndDate(String currencyId, Date date);
}
