package com.mail.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailServiceRequest {

    private String to;
    private String subject;
    private String text;

}
