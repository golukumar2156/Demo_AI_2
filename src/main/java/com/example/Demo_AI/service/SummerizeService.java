package com.example.Demo_AI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class SummerizeService {

    private final ChatClient chatClient;

    private final List<Message> history = new ArrayList<>();

    private static final String SYSTEM_PROMPT ="""
            Be a friendly, polite, helpful, clear, natural, respectful,
             conversational AI chatbot always.
             """;

    public SummerizeService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public Flux<String> chat(String message) {

        history.add(new UserMessage(message));

        StringBuilder fullResponse = new StringBuilder();

        Flux<String> response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete(() -> {
                    history.add(
                            new AssistantMessage(fullResponse.toString())
                    );
                });

        return response;
    }
}