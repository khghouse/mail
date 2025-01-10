package com.mail.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaveMailRequest {

    private String to;

    public static LeaveMailRequest of(String to) {
        LeaveMailRequest request = new LeaveMailRequest();
        request.to = to;
        return request;
    }

}
