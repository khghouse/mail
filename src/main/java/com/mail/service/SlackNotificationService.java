package com.mail.service;

import com.mail.enumeration.SlackChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SlackNotificationService {

    private static final String HEADER_PREFIX = "Bearer ";

    private final WebClient webClient;

    @Value("${slack.api.chat-post-url}")
    private String chatPostUrl;

    @Value("${slack.api.oauth-token}")
    private String oauthToken;

    public SlackNotificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(chatPostUrl)
                .build();
    }

    public void sendMessageToChannel(String message) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(chatPostUrl)
                        .queryParam("channel", SlackChannel.NOTIFICATION.getChannelId())
                        .queryParam("text", message)
                        .build()
                )
                .header(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + oauthToken)
                .retrieve()
                .bodyToMono(String.class);
    }

}
