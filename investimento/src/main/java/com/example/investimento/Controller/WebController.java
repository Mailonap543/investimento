package com.example.investimento.Controller;

import com.example.investimento.Service.StockAnalysisService;
import com.example.investimento.dto.AnalysisRequest; // DTO de entrada (formulário)
import com.example.investimento.dto.AnalysisResult;   // DTO de saída (relatório)
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Para passar dados para o HTML
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // Para receber o formulário
import org.springframework.web.bind.annotation.PostMapping; // Para processar o envio POST

@Controller
public class WebController {

    private final StockAnalysisService analysisService;

    // Injeção do serviço para usar a lógica de análise
    public WebController(StockAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/")
    public String home() {
        // Exibe o formulário inicial
        return "index";
    }

    @PostMapping("/analisar")
    public String analyze(@ModelAttribute AnalysisRequest request, Model model) {

        // 1. Executa a lógica de análise e obtém o DTO de resultado
        AnalysisResult result = analysisService.analyzeAndReport(
                request.getSymbol(),
                request.getMinYield()
        );

        // 2. Adiciona o objeto de resultado ao Model para ser acessado pelo Thymeleaf
        model.addAttribute("result", result);

        // 3. Retorna o nome da página de resultados
        return "resultado";
    }
}