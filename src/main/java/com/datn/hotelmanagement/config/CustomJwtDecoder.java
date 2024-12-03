package com.datn.hotelmanagement.config;

import java.text.ParseException;

import javax.crypto.spec.SecretKeySpec;

import com.datn.hotelmanagement.common.domain.Constants;
import com.datn.hotelmanagement.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;


@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            if (!authenticationService.verifyToken(token)) {
                SecretKeySpec wrongSecretKeySpec = new SecretKeySpec(Constants.WRONG_SIGNER_KEY.getBytes(), "HS512");
                return NimbusJwtDecoder.withSecretKey(wrongSecretKeySpec).macAlgorithm(MacAlgorithm.HS512).build()
                        .decode(token);
            }
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(Constants.SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build().decode(token);
    }
}
