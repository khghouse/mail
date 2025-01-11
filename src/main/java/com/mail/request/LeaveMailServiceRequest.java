package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaveMailServiceRequest {

    private String email;

    public static LeaveMailServiceRequest of(String email) {
        LeaveMailServiceRequest request = new LeaveMailServiceRequest();
        request.email = email;
        return request;
    }

}
