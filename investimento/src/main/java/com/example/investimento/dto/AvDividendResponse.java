package com.example.investimento.dto;

import java.util.Map;
import java.math.BigDecimal;

public class AvDividendResponse {


    public Map<String, Map<String, String>> TimeSeries;

    /**
     * Retorna o mapa de dados diários que contêm os dividendos.
     * @return Map<Data, Dados_do_dia>
     */
    public Map<String, Map<String, String>> getDividendsData() {
        return TimeSeries;
    }
}