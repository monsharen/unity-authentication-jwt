package com.unity.authentication.jwt;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@AllArgsConstructor
public class TokenValidator {

    private static final String EXPECTED_ISSUER_URL = "https://player-auth.services.api.unity.com";

    private final Date now;

    public UnityAuthentication validate(String playerId, String accessToken) throws JwkException {
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

        return UnityAuthentication.builder()
                .playerId(jwt.getSubject())
                .issuerUrl(jwt.getIssuer())
                .projectId(jwt.getClaim("project_id").asString())
                .tokenId(jwt.getClaim("jti").asString())
                .build();

    }

}