package com.talent.infusion.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWT {
    static Dotenv dotenv = Dotenv.configure().load();
    static String secretKey = dotenv.get("SECRET");

    public String generateJWT(String id, Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = secretKey.getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .setClaims(claims)
                .signWith(signingKey, signatureAlgorithm);
        return builder.compact();
    }

    public Boolean verifyJWT(String jwt) {
        String[] chunks = jwt.split(" ")[1].split("\\.");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = secretKey.getBytes();
        Key secretKeySpec = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        try {
            String tokenWithoutSignature = chunks[0] + "." + chunks[1];
            String signature = chunks[2];
            DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(signatureAlgorithm, secretKeySpec);
            return validator.isValid(tokenWithoutSignature, signature);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public int getUserId(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(jwt).getBody();

        return Integer.parseInt(claims.getId());
    }
}