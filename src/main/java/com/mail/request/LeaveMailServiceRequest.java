package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaveMailServiceRequest {

    private String email;

    public static LeaveMailServiceRequest of(String email) {
        LeaveMailServiceRequest object = new LeaveMailServiceRequest();
        object.email = email;
        return object;
    }

}
