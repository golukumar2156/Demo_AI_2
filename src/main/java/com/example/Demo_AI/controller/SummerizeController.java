package com.example.Demo_AI.controller;

import com.example.Demo_AI.service.SummerizeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SummerizeController {

    private final SummerizeService summerizeService;

    public SummerizeController(SummerizeService summerizeService) {
        this.summerizeService = summerizeService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String ticket) {
        return summerizeService.chat(ticket);
    }
}