package br.cefetmg.library.config.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.cefetmg.library.model.security.User;

@Service
public class TokenProvider {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer:library-api}")
    private String issuer;

    @Value("${api.security.token.expiration-hours:24}")
    private long expirationHours;

    public GeneratedToken generateToken(User user) {
        try {
            Instant expiration = Instant.now().plus(expirationHours, ChronoUnit.HOURS);
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getLogin())
                .withExpiresAt(expiration)
                .sign(algorithm);

            return new GeneratedToken(token, expiration);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT.", exception);
        }
    }

    public Optional<String> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String login = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();

            return Optional.ofNullable(login);
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }

    public record GeneratedToken(String token, Instant expiresAt) {
    }
}