package com.github.nut077.microservice.currencyconversionservice.controller;

import com.github.nut077.microservice.currencyconversionservice.entity.CurrencyConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class CurrencyConversionController {

    private final Environment environment;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = new CurrencyConversion(1L, from, to, BigDecimal.ONE, quantity, quantity,
                Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))));
        return ResponseEntity.ok(currencyConversion);
    }
}
