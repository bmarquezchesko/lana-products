package com.example.lana.products.service;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class EuroCurrencyFormatterService implements CurrencyFormatterService {

    @Override
    public String parse(Double total) {
        //DecimalFormatSymbols is used to indicate that comma is thousands separator and dot is decimals separator
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setGroupingSeparator(',');
        formatSymbols.setDecimalSeparator('.');
        //Decimal Format is created to add only 2 decimals, indicate currency symbol and pass format symbols
        DecimalFormat df = new DecimalFormat("###,##0.00€", formatSymbols);

        return df.format(total);
    }
}
