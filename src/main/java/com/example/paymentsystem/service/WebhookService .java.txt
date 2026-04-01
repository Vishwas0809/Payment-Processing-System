package com.example.paymentsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void send(Long senderId, Long receiverId, String amount) {
        try {
            restTemplate.postForObject(
                    "https://example.com/webhook",
                    Map.of("senderId", senderId,
                           "receiverId", receiverId,
                           "amount", amount),
                    String.class
            );
        } catch (Exception ignored) {log.error("Webhook failed", e);}
    }
}