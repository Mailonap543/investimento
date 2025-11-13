package com.example.investimento.dto;

import java.math.BigDecimal;
import java.util.Map;


public class AvQuoteResponse {


    public Map<String, Map<String, String>> globalQuote;


    public BigDecimal getLatestPrice() {
        if (globalQuote != null && globalQuote.containsKey("Global Quote")) {
            Map<String, String> quote = globalQuote.get("Global Quote");


            String priceString = quote.get("05. price");

            if (priceString != null) {
                try {
                    return new BigDecimal(priceString);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}