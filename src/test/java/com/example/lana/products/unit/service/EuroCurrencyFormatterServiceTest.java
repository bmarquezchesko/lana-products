package com.example.lana.products.unit.service;

import com.example.lana.products.service.CurrencyFormatterService;
import com.example.lana.products.service.EuroCurrencyFormatterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EuroCurrencyFormatterServiceTest {

    CurrencyFormatterService currencyFormatterService;

    @BeforeEach
    public void setUp() {
        currencyFormatterService = new EuroCurrencyFormatterService();
    }

    @Test
    void testParseDoubleWithSeveralDecimalsToStringWithTwoDecimalsAndCurrencySymbol() {
        String expected = "440.25€";
        Double total = 440.2520;

        String response = currencyFormatterService.parse(total);

        assertEquals(expected, response);
    }

    @Test
    void testParseDoubleWithoutDecimalsToStringWithTwoDecimalsAndCurrencySymbol() {
        String expected = "227.00€";
        Double total = 227.;

        String response = currencyFormatterService.parse(total);

        assertEquals(expected, response);
    }

    @Test
    void testParseDoubleWithOneDecimalToStringWithTwoDecimalsAndCurrencySymbol() {
        String expected = "107.70€";
        Double total = 107.7;

        String response = currencyFormatterService.parse(total);

        assertEquals(expected, response);
    }

    @Test
    void testParseDoubleInThousandsToStringWithCommaSeparatorsAndTwoDecimalsAndCurrencySymbol() {
        String expected = "715,789.07€";
        Double total = 715789.07123;

        String response = currencyFormatterService.parse(total);

        assertEquals(expected, response);
    }
}