package org.jon.ivmark.bet1x2.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stormpath.sdk.impl.jwt.signer.JwtSigner;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public class JwtService {

    private final JwtSigner jwtSigner;
    private final ObjectMapper objectMapper;

    public JwtService(JwtSigner jwtSigner, ObjectMapper objectMapper) {
        this.jwtSigner = jwtSigner;
        this.objectMapper = objectMapper;
    }

    public NewCookie userCookie(User user) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(user);
        String token = jwtSigner.sign(json);
        Cookie jwt = new Cookie("jwt", token, "/", null);
        return new NewCookie(jwt);
    }
}
