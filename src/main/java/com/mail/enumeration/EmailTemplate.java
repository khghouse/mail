package com.mail.enumeration;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    JOIN("가입 축하 메일", "/mail/join"),
    LEAVE("회원 탈퇴 안내 메일", "/mail/leave");

    private final String description;
    private final String templateName;

    EmailTemplate(String description, String templateName) {
        this.description = description;
        this.templateName = templateName;
    }

}
