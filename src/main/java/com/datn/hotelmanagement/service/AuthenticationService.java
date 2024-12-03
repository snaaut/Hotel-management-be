package com.datn.hotelmanagement.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import com.datn.hotelmanagement.Repository.InvalidatedTokenRepo;
import com.datn.hotelmanagement.entity.InvalidatedToken;
import com.datn.hotelmanagement.common.domain.Constants;
import com.datn.hotelmanagement.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;


@Service
public class AuthenticationService {
    @Autowired
    InvalidatedTokenRepo invalidatedTokenRepo;

    public String generateToken(Account user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("tuan1102")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", user.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(Constants.SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (KeyLengthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public Boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(Constants.SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Boolean verified = signedJWT.verify(verifier);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!verified || expiryTime.before(new Date())) {
            return false;
        }

        if (invalidatedTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            return false;
        }
        //System.out.println("TRUE");
        return true;
    }

    public InvalidatedToken saveInvalidatedToken(InvalidatedToken invalidatedToken) {
        return invalidatedTokenRepo.save(invalidatedToken);
    }
    public String getRoleFromToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        return claimsSet.getStringClaim("scope");
    }
}