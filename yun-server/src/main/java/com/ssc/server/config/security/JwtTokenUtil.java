package com.ssc.server.config.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author scx
 * @create 2022-10-13 22:16
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 验证token是否有效
     *
     * @param userDetails
     * @param token
     * @return
     */
    public boolean vaildateToken(UserDetails userDetails, String token) {
        String userName = getUserNameFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date tokenExpiredTime = getExpiredTimeFromToken(token);
        return tokenExpiredTime.before(new Date());
    }

    /**
     * 获取token过期时间
     *
     * @param token
     * @return
     */
    private Date getExpiredTimeFromToken(String token) {
        Claims calimsFromToken = getCalimsFromToken(token);
        return calimsFromToken.getExpiration();
    }

    /**
     * 是否可以刷新token
     *
     * @param token
     * @return
     */
    public boolean canRefreshToken(String token) {
        return isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims calimsFromToken = getCalimsFromToken(token);
        calimsFromToken.put(CLAIM_KEY_CREATED, new Date());
        return token;
    }

    /**
     * 生成用户信息生成token
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取用户名称
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String userName;
        Claims claims = null;
        try {
            claims = getCalimsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userName;
    }

    /**
     * 从token中获取荷载
     *
     * @param token
     * @return
     */
    private Claims getCalimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJwt(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

    /**
     * 根据荷载生成Jwt token
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }

    /**
     * 生成token失效时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
