package com.example.investimento;

import com.example.investimento.Service.StockAnalysisService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
public class InvestimentoApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvestimentoApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(StockAnalysisService stockAnalysisService) {
        return args -> {

            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;
            BigDecimal acceptableYieldThreshold = new BigDecimal("6.0");

            do {
                System.out.println("\n***************************************************");
                System.out.println("*** ANÁLISE DE APORTE DE AÇÕES (DADOS DE MERCADO) ***");
                System.out.println("***************************************************");

                System.out.println("Critério de Yield Mínimo fixado em: " + acceptableYieldThreshold.toPlainString() + "%");

                System.out.print("1. Digite o SÍMBOLO da ação B3 (Ex: PETR4.SA, SANB4.SA):\n ");
                String symbol = scanner.nextLine().trim().toUpperCase();

                System.out.println("... Buscando dados de mercado e analisando " + symbol + " ...\n");

                stockAnalysisService.analyzeAndReport(symbol, acceptableYieldThreshold);

                String resposta;
                do {
                    System.out.print("\n>>> Deseja analisar outra ação? (S/N): ");
                    resposta = scanner.nextLine().trim().toUpperCase();
                } while (!resposta.equals("S") && !resposta.equals("N"));

                if (resposta.equals("N")) {
                    continuar = false;
                }

            } while (continuar);

            System.out.println("\n*** PROGRAMA ENCERRADO. OBRIGADO! ***");
            scanner.close();
        };
    }
}