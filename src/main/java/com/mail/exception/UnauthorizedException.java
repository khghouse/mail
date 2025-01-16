package com.mail.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("인증에 필요한 값이 누락됐거나 유효하지 않습니다.");
    }

}
