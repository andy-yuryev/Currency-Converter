package com.example.currencyconverter.repository;

import com.example.currencyconverter.domain.Conversion;
import com.example.currencyconverter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    List<Conversion> findAllByUser(User user);

    List<Conversion> findAllByUserAndDate(User user, LocalDate date);
}
