package com.pangility.schwab.api.client.accountsandtrading.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * UserDetails
 * See the <a href="https://developer.schwab.com">Schwab Developer Portal</a> for more information
 */
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

    /**
     * User Type
     */
    public enum Type {
        /**
         * Advisor
         */
        ADVISOR_USER,
        /**
         * Broker
         */
        BROKER_USER,
        /**
         * Client
         */
        CLIENT_USER,
        /**
         * System
         */
        SYSTEM_USER,
        /**
         * Unknown
         */
        UNKNOWN
    }
}
