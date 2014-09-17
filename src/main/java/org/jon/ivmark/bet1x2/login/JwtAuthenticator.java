package org.jon.ivmark.bet1x2.login;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.stormpath.sdk.error.jwt.InvalidJwtException;
import com.stormpath.sdk.impl.jwt.JwtWrapper;
import com.stormpath.sdk.impl.jwt.signer.JwtSigner;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JwtAuthenticator implements Authenticator<String, User> {

    private final JwtSigner jwtSigner;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public JwtAuthenticator(JwtSigner jwtSigner, ObjectMapper objectMapper) {
        this.jwtSigner = jwtSigner;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<User> authenticate(String token) {
        try {
            JwtWrapper jwtWrapper = new JwtWrapper(token);
            validateSignature(jwtWrapper);
            Map jsonPayloadAsMap = jwtWrapper.getJsonPayloadAsMap();
            User user = objectMapper.convertValue(jsonPayloadAsMap, User.class);
            return Optional.of(user);
        } catch (InvalidJwtException e) {
            logger.warn("Invalid jwt token", e);
            return Optional.absent();
        }
    }

    private void validateSignature(JwtWrapper jwtWrapper) {
        String calculatedSignature =
                jwtSigner.calculateSignature(jwtWrapper.getBase64JwtHeader(), jwtWrapper.getBase64JsonPayload());

        if (jwtWrapper.getBase64JwtSignature().equals(calculatedSignature)) {
            return;
        }
        throw new InvalidJwtException(InvalidJwtException.INVALID_JWT_SIGNATURE_ERROR);
    }
}