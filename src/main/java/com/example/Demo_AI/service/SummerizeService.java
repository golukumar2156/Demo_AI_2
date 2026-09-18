package com.example.Demo_AI.service;
import com.example.Demo_AI.aiTools.CalculatorTool;
import com.example.Demo_AI.aiTools.currencyExchange.CurrencyExchangeTool;
import com.example.Demo_AI.aiTools.weather.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummerizeService {

    private final ChatClient chatClient;
    private final CalculatorTool calculatorTool;
    private  final WeatherTool weatherTool;
    private final CurrencyExchangeTool currencyExchangeTool;
    private List<Message> history=new ArrayList<>();

    private final String SYSTEM_PROMPT = """
        You are a helpful AI assistant with access to external tools.
        Follow these rules:
        1. For arithmetic calculations, ALWAYS use the calculator tool.
        2. For weather queries, use the weather tool.
        3. For currency conversion, use the currency exchange tool.
        4. Always respond in plain, simple text only.
        5. Never use LaTeX notation (no \\[ \\], \\times, \\textbf{}, or any math markup).
        6. Never use Markdown formatting like **bold**, headers, or bullet symbols.
        7. Write numbers normally (e.g. "10,000" not "10\\,000").
        8. Keep responses short, clear, and conversational — like a normal chat message.
        """;
    public SummerizeService(ChatClient.Builder builder,
                            CalculatorTool calculatorTool,
                            WeatherTool weatherTool,
                            CurrencyExchangeTool currencyExchangeTool
    ) {
        this.chatClient = builder.build();
        this.calculatorTool=calculatorTool;
        this.weatherTool=weatherTool;
        this.currencyExchangeTool=currencyExchangeTool;
    }

    public String chat(String message) {

        history.add(new UserMessage(message));
        String response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .tools(calculatorTool,weatherTool,currencyExchangeTool)
                .call()
                .content();
        history.add(new AssistantMessage(response));
        return response;
    }


}
