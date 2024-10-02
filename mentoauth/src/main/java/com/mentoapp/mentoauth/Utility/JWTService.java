package com.mentoapp.mentoauth.Utility;

import com.mentoapp.mentoauth.Service.AuthService.DTOs.TokenResponse;
import com.mentoapp.mentoauth.Service.AuthService.DTOs.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.ldap.userdetails.Person;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String secret;

    public String extractEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwtToken).getPayload();
    }

    public SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public TokenResponse generateToken(String email, Set<UserRole> roles) {
        return createToken(email, roles);
    }

    public TokenResponse createToken(String email, Set<UserRole> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("roles", roles);
        String token = Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey())
                .compact();

        TokenResponse refreshToken = TokenResponse.builder().token(Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 36))
                .signWith(getSigningKey())
                .compact())
                .expireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 36)).build();

        return TokenResponse.builder()
                .token(token)
                .expireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .refreshToken(refreshToken)
                .build();
    }
}
