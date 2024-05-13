package com.app.Authentication.Authorization.security;

import static com.app.Authentication.Authorization.util.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
	private static final String SECRET_KEY = "03660bc3b1d80451fa15edf510ef9348a588f350029d5ea0d011f600e0e7b593";

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSigninKey() {
		byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(User userDetails) {
		return generateToken(Map.of(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaim, User userDetails) {
		return Jwts.builder().setClaims(extraClaim).setSubject(userDetails.getUsername())
				.setIssuer("http://ebraintechnologies.com").setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
				.signWith(getSigninKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())) && !istokenexpired(token);
	}

	private boolean istokenexpired(String token) {
		return extractExpriation(token).before(new Date());
	}

	private Date extractExpriation(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public boolean validateToken(String token) {
		return !istokenexpired(token);
	}

	public String extractToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	public UUID getUserIdFromToken(String token) {
		try {
			// Decode JWT token using the secret key
			Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET_KEY.getBytes()));
//            Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).build().parseClaimsJws(token).getBody();

			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

			// Extract user ID from claims
//            String userId = claims.get("user_id", String.class);
//            return userId;
			return UUID.fromString(claims.get("user_id", String.class));
		} catch (SignatureException e) {
			// Handle signature verification failure
			throw new SignatureException("Signature verification failed for the JWT token.");
		} catch (Exception e) {
			// Handle exceptions
			e.printStackTrace();
			throw new RuntimeException("Failed to get user ID from token.");
		}
	}

	public String getUseeNmaeFromToken(String token) {
		try {
			// Parse the JWT token and extract user details
			Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET_KEY.getBytes()));
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

			// Extract user details from claims
			String username = claims.getBody().getSubject(); // Assuming username is stored as subject

			return username;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to get user from token.");
		}
	}

	public  String getUsernameFromToken(String token) {
        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token);
        return jwsClaims.getBody().getSubject();
    }
}
