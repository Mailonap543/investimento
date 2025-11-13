package com.example.investimento.Service;

import com.example.investimento.dto.AnalysisResult; // NOVO IMPORT
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Service
public class StockAnalysisService {


    private final MarketDataService marketDataService;

    public StockAnalysisService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    // A assinatura do método MUDOU: agora retorna AnalysisResult
    public AnalysisResult analyzeAndReport(String symbol, BigDecimal acceptableYieldThreshold) {

        int numberOfDividendsForAverage = 5;
        String verdict;
        String message = "";

        BigDecimal currentPrice = marketDataService.getCurrentPrice(symbol);

        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            verdict = "N/A";
            message = "ERRO: Não foi possível obter o preço atual ou o símbolo '" + symbol + "' é inválido/não mapeado.";

            // Retorna um objeto de resultado de erro
            return new AnalysisResult(symbol, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    acceptableYieldThreshold, verdict, message);
        }

        List<BigDecimal> lastDividends = marketDataService.getHistoricalDividends(symbol, numberOfDividendsForAverage);

        // Se os dados de dividendos falharem, ainda podemos exibir o preço, mas o cálculo fica comprometido
        if (lastDividends.isEmpty()) {
            message = "AVISO: Sem dados históricos de dividendos para análise. Usando dados simulados.";
        }


        // ***************************************************************
        // ** CÁLCULO DO DIVIDEND YIELD (Lógica de Fator Compensatório) **
        // ***************************************************************

        BigDecimal averageDividend = BigDecimal.ZERO;
        // Usa a média dos dados da API, ou zero se a API falhou
        if (!lastDividends.isEmpty()) {
            BigDecimal sumOfDividends = lastDividends.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            averageDividend = sumOfDividends.divide(
                    BigDecimal.valueOf(lastDividends.size()),
                    8,
                    RoundingMode.HALF_UP
            );
        } else {
            // Caso a API de dividendos falhe (retorna emptyList), precisamos simular
            // uma média para que o cálculo a seguir não falhe com zero. Usaremos 0.15 como default.
            averageDividend = new BigDecimal("0.15");
            message += " Usando média de dividendo simulada de R$ 0.15.";
        }

        // FATOR DE ANUALIZAÇÃO COMPENSATÓRIO: Usamos 9.0 para compensar a falta de JCP
        // e aproximar o DY Projetado do valor real de mercado.
        BigDecimal annualizationFactor = new BigDecimal("9.0");

        BigDecimal annualizedDividend = averageDividend.multiply(annualizationFactor);

        BigDecimal projectedDividendYield = annualizedDividend
                .divide(currentPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        // ***************************************************************
        // ** VEREDICTO **
        // ***************************************************************
        if (projectedDividendYield.compareTo(acceptableYieldThreshold) > 0) {
            verdict = "BOM PREÇO PARA APORTE!";
        } else {
            verdict = "PREÇO NÃO IDEAL PARA APORTE.";
        }


        return new AnalysisResult(symbol, currentPrice, averageDividend,
                projectedDividendYield, acceptableYieldThreshold,
                verdict, message);
    }
}