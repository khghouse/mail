package com.mail.event;

import com.mail.response.SlackResponse;
import com.mail.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpAuthenticationFailureListener {

    private final SlackService slackService;

    @EventListener
    public SlackResponse onSmtpAuthenticationFailure(SmtpAuthenticationFailureEvent event) {
        return slackService.send(event.getErrorMessage());
    }

}
