package com.unity.authentication.jwt;

import com.auth0.jwk.JwkException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
public class TokenValidatorTest {

    private TokenValidator instance;

    @BeforeEach
    public void BeforeEach() {
        Date date = new Date(1681207053000L);
        instance = new TokenValidator(date);
    }

    @Test
    public void test() throws JwkException {
        String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzpBNTYwOTVEQS0xODJDLTQ1MjMtOUQyNS1DNzlEMz" +
                "NBNEY5OUIiLCJ0eXAiOiJKV1QifQ.eyJhdWQiOlsiaWRkOjhhMzJlNGY4LTk4MGUtNDVhMS1iMjcyLWM5YzA0OTk3ND" +
                "IwOSIsImVudk5hbWU6cHJvZHVjdGlvbiIsImVudklkOjQ0MGQ3NTgxLWYxNDYtNDE3NS04NzYwLTVjMjUwZDIwMDZmO" +
                "SIsInVwaWQ6YjZkMzhhNDQtMzVhNC00ZDIwLTkwOWItNTA2OTZhZmNmZjM5Il0sImV4cCI6MTY4MTIxMDY1MywiaWF0" +
                "IjoxNjgxMjA3MDUzLCJpZGQiOiI4YTMyZTRmOC05ODBlLTQ1YTEtYjI3Mi1jOWMwNDk5NzQyMDkiLCJpc3MiOiJodHR" +
                "wczovL3BsYXllci1hdXRoLnNlcnZpY2VzLmFwaS51bml0eS5jb20iLCJqdGkiOiJjYzkzNTExZC04NjI1LTRmMzYtOT" +
                "k5Ny1hY2UwODU0YjdkNGQiLCJuYmYiOjE2ODEyMDcwNTMsInByb2plY3RfaWQiOiJiNmQzOGE0NC0zNWE0LTRkMjAtO" +
                "TA5Yi01MDY5NmFmY2ZmMzkiLCJzaWduX2luX3Byb3ZpZGVyIjoiYW5vbnltb3VzIiwic3ViIjoiRnFwVFhMdVZFYWJj" +
                "WmJKcWFXazQ2S09uZmhNQSIsInRva2VuX3R5cGUiOiJhdXRoZW50aWNhdGlvbiIsInZlcnNpb24iOiIxIn0.Aq8c2K7" +
                "JRc1H6GL77yQwmnLQeiC4Yp4PxSb3VNMD_PwcNFfS50gp4sd83I5BJzUQ0Y3EPRK4lkfyPV3xFQRxTtfgRSXt2_xiXe" +
                "eOVAe4QQ2pDcT0FBvUkVPYl4QmKAD8sKBpZ82slBtb6PqdQjUn8U5-vEp5lSWKRHXCte4LysD3ud_xrWZjO52KgeO3O" +
                "K-hyCpmXj5LZNiDzeRDJRUKsVH4hRZboIftMw-bpP4YsoRbUf_WlelrYmgUhp6Ss6ZHr_kMQw-pwOjL4GdU13qzLDSX" +
                "8K5lt9W7P3envhN6J4diUMP8tG1tK9pT1ms0Vf9PNs_tODnsGpKyDxV2XlLdjA";

        String playerId = "FqpTXLuVEabcZbJqaWk46KOnfhMA";

        UnityAuthentication unityAuthentication = instance.validate(playerId, accessToken);

        assertEquals("FqpTXLuVEabcZbJqaWk46KOnfhMA", unityAuthentication.getPlayerId(), "unexpected player id");
        assertEquals("b6d38a44-35a4-4d20-909b-50696afcff39", unityAuthentication.getProjectId(), "unexpected project id");
        assertEquals("https://player-auth.services.api.unity.com", unityAuthentication.getIssuerUrl(), "unexpected issuer url");
    }
}
