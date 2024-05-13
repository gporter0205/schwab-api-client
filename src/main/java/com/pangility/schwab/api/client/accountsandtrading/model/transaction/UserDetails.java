package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDetails {
    private String cdDomainId;
    private String login;
    private Type type;
    private String systemUserName;
    private String firstName;
    private String lastName;
    private String brokerRepCode;

    public enum Type {
        ADVISOR_USER, BROKER_USER, CLIENT_USER, SYSTEM_USER, UNKNOWN
    }
}
