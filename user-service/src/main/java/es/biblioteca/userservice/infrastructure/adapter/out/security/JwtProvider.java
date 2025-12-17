package es.biblioteca.userservice.infrastructure.adapter.out.security;

import es.biblioteca.userservice.infrastructure.config.ConfigJwtProperties;
import es.biblioteca.userservice.infrastructure.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtProvider {

    private final ConfigJwtProperties configJwtProperties;

    public JwtProvider(ConfigJwtProperties configJwtProperties) {
        super();
        this.configJwtProperties = configJwtProperties;
    }

    public TokenResponse createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Puedes extraer roles y otros datos de userDetails si es necesario
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return new TokenResponse(buildToken(claims,userDetails));
    }

    public boolean isTokenValid(TokenResponse token, UserDetails userDetails) {
        final String username = extractUsername(token.token());
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token.token());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- MÉTODOS PRIVADOS ---

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(configJwtProperties.getExpiration(), ChronoUnit.MILLIS);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryInstant))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException _) {
            // Manejar token inválido (malformado, expirado, etc.)
            throw new BadCredentialsException("Invalid JWT token");
        }
    }

    private SecretKey getSignInKey() {
        // La clave secreta debe estar en Base64 para ser robusta
        byte[] keyBytes = Decoders.BASE64.decode(configJwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}