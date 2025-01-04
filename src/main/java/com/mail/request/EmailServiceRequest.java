package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailServiceRequest {

    private String to;
    private String subject;
    private String text;

    public static EmailServiceRequest of(String to, String subject, String text) {
        EmailServiceRequest request = new EmailServiceRequest();
        request.to = to;
        request.subject = subject;
        request.text = text;
        return request;
    }

}
