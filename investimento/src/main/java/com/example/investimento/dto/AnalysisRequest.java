package com.example.investimento.dto;

import java.math.BigDecimal;

public class AnalysisRequest {

    private String symbol;
    private BigDecimal minYield; // O nome deve ser 'minYield' para coincidir com o campo do formulário

    // Construtor padrão (necessário pelo Spring)
    public AnalysisRequest() {}

    // Getters e Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol.toUpperCase();
    }

    public BigDecimal getMinYield() {
        return minYield;
    }

    public void setMinYield(BigDecimal minYield) {
        this.minYield = minYield;
    }
}