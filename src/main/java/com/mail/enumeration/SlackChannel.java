package com.mail.enumeration;

import lombok.Getter;

@Getter
public enum SlackChannel {

    NOTIFICATION("C089MA18UNL");

    private final String channelId;

    SlackChannel(String channelId) {
        this.channelId = channelId;
    }

}
