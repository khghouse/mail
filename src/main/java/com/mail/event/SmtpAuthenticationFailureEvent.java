package com.mail.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmtpAuthenticationFailureEvent {

    private String errorMessage;

    public static SmtpAuthenticationFailureEvent create(String errorMessage) {
        SmtpAuthenticationFailureEvent object = new SmtpAuthenticationFailureEvent();
        object.errorMessage = errorMessage;
        return object;
    }

}
