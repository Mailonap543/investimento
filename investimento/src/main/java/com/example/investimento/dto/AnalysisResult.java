package com.example.investimento.dto;

import java.math.BigDecimal;

public class AnalysisResult {

    private String symbol;
    private BigDecimal currentPrice;
    private BigDecimal averageDividend;
    private BigDecimal projectedDividendYield;
    private BigDecimal acceptableYieldThreshold;
    private String verdict;
    private String message; // Para erros ou avisos

    // Construtor
    public AnalysisResult(String symbol, BigDecimal currentPrice, BigDecimal averageDividend,
                          BigDecimal projectedDividendYield, BigDecimal acceptableYieldThreshold,
                          String verdict, String message) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.averageDividend = averageDividend;
        this.projectedDividendYield = projectedDividendYield;
        this.acceptableYieldThreshold = acceptableYieldThreshold;
        this.verdict = verdict;
        this.message = message;
    }

    // --- Getters (Setters não são estritamente necessários se o objeto for construído)
    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getAverageDividend() {
        return averageDividend;
    }

    public BigDecimal getProjectedDividendYield() {
        return projectedDividendYield;
    }

    public BigDecimal getAcceptableYieldThreshold() {
        return acceptableYieldThreshold;
    }

    public String getVerdict() {
        return verdict;
    }

    public String getMessage() {
        return message;
    }
}