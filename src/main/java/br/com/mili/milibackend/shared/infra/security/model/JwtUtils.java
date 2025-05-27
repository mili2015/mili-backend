package br.com.mili.milibackend.shared.infra.security.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JwtUtils {

    @Value("${mili.auth.secret}")
    private String secret;

    public boolean validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            JWTVerifier verifier = JWT.require(algorithm).build();

            verifier.verify(token);

            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String extractUsername(String Token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT jwt = JWT.require(algorithm).build().verify(Token);


            jwt.getClaim("username").asString();
            return jwt.getSubject();

        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    public List<Roles> extractRoles(String Token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT jwt = JWT.require(algorithm).build().verify(Token);

            var roles = jwt.getClaim("roles");
            ObjectMapper mapper = new ObjectMapper();


            return mapper.convertValue(roles.as(Object.class), new TypeReference<List<Roles>>() {
            });
        } catch (JWTVerificationException ex) {
            return null;
        }
    }
}
