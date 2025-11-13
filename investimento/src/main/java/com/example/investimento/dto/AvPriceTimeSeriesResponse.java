package com.example.investimento.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;


public class AvPriceTimeSeriesResponse {


    public Map<String, Map<String, String>> TimeSeries;



    public BigDecimal getLatestClosePrice() {
        if (TimeSeries == null || TimeSeries.isEmpty()) {
            return null;
        }

        // Encontra o mapa de dados do dia mais recente (primeira entrada do mapa)
        Optional<Map<String, String>> latestData = TimeSeries.values().stream().findFirst();

        if (latestData.isPresent()) {
            // O valor de fechamento é o campo '4. close'
            String priceString = latestData.get().get("4. close");

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