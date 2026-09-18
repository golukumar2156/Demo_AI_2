package com.example.Demo_AI.aiTools.currencyExchange;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CurrencyExchangeTool {

    private final RestClient restClient;

    public CurrencyExchangeTool(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.frankfurter.dev")
                .build();
    }

    @Tool(description = "Gets the latest exchange rate " +
            "between two currencies.")
    public String getExchangeRate(
            @ToolParam(description = "base currency code, e.g. USD, INR, EUR")
            String from,
            @ToolParam(description = "target currency code, e.g. USD, INR, EUR")
            String to) {
        System.out.println("Currency Exchange Tool called: " + from + " -> " + to);

        return restClient.get()
                .uri("/v2/rate/{from}/{to}", from, to)
                .retrieve()
                .body(String.class);
    }
}