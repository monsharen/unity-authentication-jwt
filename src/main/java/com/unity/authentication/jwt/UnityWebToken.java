package com.unity.authentication.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class UnityWebToken {

    private final String playerId;
    private final String projectId;
    private final String tokenId;
    private final String issuerUrl;
    private final Audience audience;
    private final String signInProvider;
    private final String tokenType;
}
