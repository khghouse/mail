package com.mail.service;

import com.mail.enumeration.SlackChannel;
import com.mail.response.SlackResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SlackService {

    private static final String HEADER_PREFIX = "Bearer ";

    @Value("${slack.api.oauth-token}")
    private String oauthToken;

    @Value("${slack.api.chat-post-url}")
    private String chatPostUrl;

    @Value("${slack.api.conversations-info-url}")
    private String conversationsInfoUrl;

    public SlackResponse send(String message) {
        return WebClient.builder()
                .baseUrl(chatPostUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + oauthToken)
                .build()
                .post()
                .bodyValue(Map.of(
                        "channel", SlackChannel.NOTIFICATION.getChannelId(),
                        "text", message
                ))
                .retrieve()
                .bodyToMono(SlackResponse.class)
                .doOnNext(response -> {
                    if (!response.isOk()) {
                        throw new RuntimeException("Slack API Error: " + response);
                    }
                }).block();
    }

    public SlackResponse checkSlackChannel(String channel) {
        return WebClient.builder()
                .baseUrl(conversationsInfoUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + oauthToken)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("channel", channel)
                        .build()
                )
                .retrieve()
                .bodyToMono(SlackResponse.class)
                .doOnNext(response -> {
                    if (!response.isOk()) {
                        throw new RuntimeException("Slack API Error: " + response);
                    }
                }).block();
    }

}
