package com.dailystudy.dtmsapi.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${app.jwt-secure-key}")
    private String secretKey;
    @Value("${app.jwt-secure-report-key}")
    private String reportSecureKey;
    private String encodedSecretKey;
    private String encodedReportSecretKey;
    private Key signKey;
    private Key reportSignKey;    ;

    private final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L;   // 1시간
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 1000L;   // 1일
    private final long NORMAL_TOKEN_VALID_TIME = 60 * 60 * 24 * 365 * 1000L; //1년
    private final long REPORT_ACCESS_TOKEN_VALID_TIME = 60 * 60 * 24 * 30 * 1000L; //30일
    private final long RE_CREATE_TOKEN_VALID_TIME = 60 * 60 * 1000L;   // 1시간

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final MemberDetailService memberDetailService;

    public enum TokenValidResultType {
        notExpired,
        needNewToken,
        needLogin
    }

    @PostConstruct
    protected void init() {
        encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        signKey = new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName());

        encodedReportSecretKey = Base64.getEncoder().encodeToString(reportSecureKey.getBytes());
        reportSignKey = new SecretKeySpec(reportSecureKey.getBytes(), signatureAlgorithm.getJcaName());
    }

    public String createJwtAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(signKey, signatureAlgorithm)
                .compact();
    }

    public String createReportJwtAccessToken(String id, String studentNumber) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REPORT_ACCESS_TOKEN_VALID_TIME);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("studentNumber", studentNumber);

        return Jwts.builder()
                .setClaims(map)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(reportSignKey, signatureAlgorithm)
                .compact();
    }

    public String createJwtRefreshToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setSubject("")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(signKey, signatureAlgorithm)
                .compact();
    }

    public String createJwtNormalToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + NORMAL_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setSubject("")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(signKey, signatureAlgorithm)
                .compact();
    }

    public String resolveJwtToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public String resolveReportJwtToken(HttpServletRequest request) {
        return request.getParameter("key");
    }

    public Authentication getAuthentication(String token, HttpServletRequest request) {
        UserDetails userDetails = memberDetailService.loadUserByUsername(this.getUserId(token), request);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }


//    public Authentication getReportAuthentication(String token, HttpServletRequest request) {
//        UserDetails userDetails = memberDetailService.loadUserByReportUser(this.getReportPayload(token), request);
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    public String getUserId(String token) {
        return getClaimsFromJwtToken(token).getBody().getSubject();
    }

    public Map<String, Object> getReportPayload(String token) {
        Map<String, Object> map = new HashMap<>();

        Claims claims = getClaimsFromReportJwtToken(token).getBody();
        map.put("id", claims.get("id") != null? claims.get("id").toString() : "0");
        map.put("studentNumber", claims.get("studentNumber") != null? claims.get("studentNumber").toString() : "0");

        return map;
    }

    public TokenValidResultType validateToken(String jwtToken) {
        try {
            getClaimsFromJwtToken(jwtToken);

            return TokenValidResultType.notExpired;
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            Date expiration = claims.getExpiration();



            long expirationMilliseconds = expiration.getTime();
            long currentMilliseconds = (new Date()).getTime();

            System.out.println("exp:" + expirationMilliseconds);

                       System.out.println("current:" + currentMilliseconds);
                       System.out.println("re:" + RE_CREATE_TOKEN_VALID_TIME);
            if(expirationMilliseconds + RE_CREATE_TOKEN_VALID_TIME > currentMilliseconds) {
                return TokenValidResultType.needNewToken;
            }

            return TokenValidResultType.needLogin;
        } catch (Exception e) {
            return TokenValidResultType.needLogin;
        }
    }

    public boolean isReportTokenValid(String jwtToken) {
        try {
            Jws<Claims> claims = getClaimsFromReportJwtToken(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> getClaimsFromJwtToken(String jwtToken) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(encodedSecretKey).build().parseClaimsJws(jwtToken);
    }

    public Jws<Claims> getClaimsFromReportJwtToken(String jwtToken) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(encodedReportSecretKey).build().parseClaimsJws(jwtToken);
    }
}
