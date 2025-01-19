package com.mail.service;

import com.mail.IntegrationTestSupport;
import com.mail.response.SlackResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SlackNotificationServiceTest extends IntegrationTestSupport {

    @Autowired
    private SlackNotificationService slackNotificationService;

    @Test
    @DisplayName("슬랙 채널로 메시지를 발송한다.")
    void send() {
        // given
        String message = "슬랙 채널로 발송하는 메시지입니다.";

        // when
        SlackResponse response = slackNotificationService.send(message);

        // then
        assertThat(response.isOk()).isTrue();
    }

    @Test
    @DisplayName("슬랙 oauthToken이 유효하지 않다면 예외가 발생한다.")
    void sendInavlidAuth() {
        // given
        String message = "슬랙 채널로 발송하는 메시지입니다.";
        ReflectionTestUtils.setField(slackNotificationService, "oauthToken", "invalidOauthToken");

        // when, then
        assertThatThrownBy(() -> slackNotificationService.send(message))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("api url이 유효하지 않다면 예외가 발생한다.")
    void sendInvalidChatPostUrl() {
        // given
        String message = "슬랙 채널로 발송하는 메시지입니다.";
        ReflectionTestUtils.setField(slackNotificationService, "chatPostUrl", "https://slack.com/invalid/url");

        // when, then
        assertThatThrownBy(() -> slackNotificationService.send(message))
                .isInstanceOf(RuntimeException.class);
    }

}