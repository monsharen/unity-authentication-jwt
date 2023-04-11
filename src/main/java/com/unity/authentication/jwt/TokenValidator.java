package com.unity.authentication.jwt;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class TokenValidator {

    private static final String EXPECTED_ISSUER_URL = "https://player-auth.services.api.unity.com";

    private final Date now;

    public UnityWebToken validate(String playerId, String accessToken) throws JwkException {
        DecodedJWT jwt = JWT.decode(accessToken);

        JwkProvider provider = new UrlJwkProvider("https://api.prd.identity.corp.unity3d.com");
        Jwk jwk = provider.get(jwt.getKeyId());

        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        algorithm.verify(jwt);

        if (jwt.getNotBefore().after(now)) {
            throw new RuntimeException("Token is not yet valid");
        }

        if (jwt.getExpiresAt().before(now)) {
            throw new RuntimeException("Token has expired");
        }

        if (!playerId.equals(jwt.getSubject())) {
            throw new RuntimeException(String.format("Provided Player ID '%s' does not match Player ID in token", playerId));
        }

        String issuerUrl = jwt.getIssuer();
        if (!EXPECTED_ISSUER_URL.equals(issuerUrl)) {
            throw new RuntimeException(String.format("Unexpected issuer url '%s'", issuerUrl));
        }

        String projectId = getClaim(jwt, "project_id");

        String tokenId = getClaim(jwt, "jti");

        String tokenType = getClaim(jwt, "token_type");

        String signInProvider = getClaim(jwt, "sign_in_provider");

        Audience audience = parseAudience(jwt);

        return UnityWebToken.builder()
                .playerId(jwt.getSubject())
                .issuerUrl(jwt.getIssuer())
                .projectId(projectId)
                .tokenId(tokenId)
                .audience(audience)
                .tokenType(tokenType)
                .signInProvider(signInProvider)
                .build();
    }

    private Audience parseAudience(DecodedJWT jwt) {
        List<String> audience = jwt.getAudience();

        Audience.AudienceBuilder audienceBuilder = Audience.builder();
        for (String aud : audience) {
            if (aud.contains(":")) {
                String[] parts = aud.split(":");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    if ("envName".equals(key)) {
                        audienceBuilder.environmentName(value);
                    } else if ("envId".equals(key)) {
                        audienceBuilder.environmentId(value);
                    }
                }
            }
        }

        return audienceBuilder.build();
    }

    private String getClaim(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);
        if (claim == null) {
            throw new IllegalStateException(String.format("jwt did not contain claim with key '%s'", key));
        }

        return claim.asString();
    }

}
