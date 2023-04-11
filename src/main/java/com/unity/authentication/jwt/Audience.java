package com.unity.authentication.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Audience {

    private final String environmentId;
    private final String environmentName;
}
