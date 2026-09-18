package com.example.Demo_AI.aiTools.weather;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WeatherTool {

    private final RestClient restClient;
    private final String apiKey;

    public WeatherTool(RestClient.Builder builder,
                       @Value("${weather.api.base-url}") String baseUrl,
                       @Value("${weather.api.key}") String apiKey) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    @Tool(
            description = """
                 Get the current weather for a given city.
                 """
    )
    public String getWeather(
            @ToolParam(description = "city name, e.g. Indore, Mumbai")
            String city) {
        System.out.println("Weather Tool called for city: " + city);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current.json")
                        .queryParam("key", apiKey)
                        .queryParam("q", city)
                        .build())
                .retrieve()
                .body(String.class);
    }
}