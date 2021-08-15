package com.example.lana.products.service;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

@Service
public class EuroCurrencyFormatter implements CurrencyFormatter{

    @Override
    public String parse(Double total) {
        String currencySymbol = Currency.getInstance("EUR").getSymbol(Locale.UK);
        DecimalFormat df = new DecimalFormat("#,##0.00" + currencySymbol);

        return df.format(total);
    }
}
