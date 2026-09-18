package com.example.Demo_AI.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo_AI.service.SummerizeService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class SummerizeController {

    private final SummerizeService summerizeService;

    public SummerizeController(SummerizeService summerizeService) {
        this.summerizeService = summerizeService;
    }

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody String ticket) {
        return summerizeService.chat(ticket);
    }
}