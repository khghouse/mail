package com.mail.event;

import com.mail.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpAuthenticationFailureListener {

    private final SlackService slackService;

    @Async
    @EventListener
    public void onSmtpAuthenticationFailure(SmtpAuthenticationFailureEvent event) {
        slackService.send(event.getErrorMessage());
    }

}
