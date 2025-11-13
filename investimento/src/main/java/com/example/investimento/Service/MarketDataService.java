package com.example.investimento.Service;

import com.example.investimento.dto.AvDividendResponse;
import com.example.investimento.dto.AvPriceTimeSeriesResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MarketDataService {

    private static final String API_KEY = "1TS9EOT16T3K980A";
    private static final String ALPHA_VANTAGE_URL = "https://www.alphavantage.co/query";

    private final WebClient webClient;

    public MarketDataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ALPHA_VANTAGE_URL).build();
    }


    public BigDecimal getCurrentPrice(String symbol) {

        String baseSymbol = symbol.replace(".SA", "").replace(".SAO", "");

        // Tentativas de símbolo (ex: PETR4.SA, PETR4.SAO, PETR4)
        List<String> symbolAttempts = Arrays.asList(
                symbol,
                baseSymbol + ".SAO",
                baseSymbol
        );

        for (String currentSymbol : symbolAttempts) {
            try {
                // Tenta buscar o preço usando a função TIME_SERIES_DAILY
                AvPriceTimeSeriesResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("function", "TIME_SERIES_DAILY")
                                .queryParam("symbol", currentSymbol)
                                .queryParam("outputsize", "compact")
                                .queryParam("apikey", API_KEY)
                                .build())
                        .retrieve()
                        .bodyToMono(AvPriceTimeSeriesResponse.class)
                        .block();

                // Se o preço for encontrado, retorna imediatamente.
                if (response != null && response.getLatestClosePrice() != null) {
                    // MENSAGEM REMOVIDA
                    return response.getLatestClosePrice();
                }
            } catch (Exception e) {
                // MENSAGEM REMOVIDA
            }
        }

        // --- FALLBACK (Contingência) ---
        // MENSAGEM DE AVISO REMOVIDA
        Random random = new Random(symbol.hashCode() + 10);
        // Simula um preço entre R$ 15.00 e R$ 45.00
        double simulatedValue = 15.00 + (45.00 - 15.00) * random.nextDouble();
        return new BigDecimal(simulatedValue).setScale(2, RoundingMode.HALF_UP);
    }


    public List<BigDecimal> getHistoricalDividends(String symbol, int n) {
        try {
            // Tenta buscar da API
            AvDividendResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("function", "TIME_SERIES_DAILY_ADJUSTED")
                            .queryParam("symbol", symbol)
                            .queryParam("outputsize", "full")
                            .queryParam("apikey", API_KEY)
                            .build())
                    .retrieve()
                    .bodyToMono(AvDividendResponse.class)
                    .block();

            Map<String, Map<String, String>> dailyData = response != null && response.TimeSeries != null
                    ? response.TimeSeries
                    : Collections.emptyMap();


            if (!dailyData.isEmpty()) {
                List<BigDecimal> dividendValues = new ArrayList<>();

                for (Map.Entry<String, Map<String, String>> entry : dailyData.entrySet()) {
                    String dividendAmountString = entry.getValue().get("7. dividend amount");

                    if (dividendAmountString != null && !dividendAmountString.equals("0.0000") && !dividendAmountString.equals("0")) {
                        try {
                            BigDecimal dividendValue = new BigDecimal(dividendAmountString);
                            dividendValues.add(dividendValue);


                            if (dividendValues.size() >= n) {
                                return dividendValues;
                            }
                        } catch (NumberFormatException ignored) {
                            // Ignora valores não numéricos
                        }
                    }
                }
                return dividendValues;
            }

        } catch (Exception e) {
            // MENSAGEM DE AVISO REMOVIDA
        }

        // --- FALLBACK (Contingência) ---
        Random random = new Random(symbol.hashCode());
        List<BigDecimal> simulatedDividends = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            double rawValue = 0.05 + (0.35 - 0.05) * random.nextDouble();
            BigDecimal dividend = new BigDecimal(rawValue).setScale(4, RoundingMode.HALF_UP);
            simulatedDividends.add(dividend);
        }
        return simulatedDividends;
    }
}