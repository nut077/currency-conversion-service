package com.github.nut077.microservice.currencyconversionservice.controller;

import com.github.nut077.microservice.currencyconversionservice.entity.CurrencyConversion;
import com.github.nut077.microservice.currencyconversionservice.exception.NotFoundException;
import com.github.nut077.microservice.currencyconversionservice.service.CurrencyExchangeServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@RestController
public class CurrencyConversionController {

    private final CurrencyExchangeServiceProxy proxy;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables);

        CurrencyConversion response = responseEntity.getBody();
        if (Objects.requireNonNull(response).getId() == null) {
            throw new NotFoundException("can't not find currency exchange from: " + from + " to: " + to);
        }
        CurrencyConversion currencyConversion = new CurrencyConversion(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()),
                response.getPort());
        return ResponseEntity.ok(currencyConversion);
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        CurrencyConversion response = proxy.retrieveExchangeValue(from, to);
        if (Objects.requireNonNull(response).getId() == null) {
            throw new NotFoundException("can't not find currency exchange from: " + from + " to: " + to);
        }
        CurrencyConversion currencyConversion = new CurrencyConversion(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()),
                response.getPort());
        log.info("{}", response);
        return ResponseEntity.ok(currencyConversion);
    }
}
