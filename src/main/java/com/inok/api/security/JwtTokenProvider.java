package com.inok.api.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

	@Value("${spring.jwt.secret}")
	private String secretKey;

	private long tokenValidMilisecond = 1000L * 60 * 60 * 24; // 24시간만 토큰 유효

//	private final UserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// Jwt 토큰 생성
	public String createToken(String id, String name) {
		Claims claims = Jwts.claims().setSubject(id);
		claims.put("name", name);
		Date now = new Date();
		String jwt = Jwts.builder().setClaims(claims).setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
		return jwt;
	}

	public Map<String, Object> getUserParseInfo(String token) {
		Jws<Claims> parseInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		Map<String, Object> result = new HashMap<>();
		result.put("id", parseInfo.getBody().getSubject());
		result.put("name", parseInfo.getBody().get("name", String.class));
		return result;
	}

	// Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
	public String resolveToken(HttpServletRequest req) {
		return req.getHeader("X-AUTH-TOKEN");
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (ExpiredJwtException eje) {
			// 토큰 유효기간 만료
			throw eje;
		} catch (SignatureException se) {
			// 토큰 서명 검증이 위조 or 문제
			throw se;
		} catch (Exception e) {
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		Map<String, Object> parseInfo = getUserParseInfo(token);
        return new UsernamePasswordAuthenticationToken(parseInfo.get("id"), "");
	}
}
