package com.happy.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @author LeiJJ
 * @date 2021-11-05 21:06
 */
public class TestJWT {

    //过期时间，毫秒，24小时
    private static long tokenExpiration = 5*60*1000;
    //秘钥
    private static String tokenSignKey = "jason123";

    @Test
    public void testCreateToken(){
        String token = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setHeaderParam("alg", "HS256")

                .setSubject("jason-user")
                .setIssuer("jason")
                .setAudience("jason")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .setNotBefore(new Date(System.currentTimeMillis() + 20 * 1000))
                .setId(UUID.randomUUID().toString())

                .claim("nickname", "zhongli")
                .claim("avatar", "1.jpg")

                .signWith(SignatureAlgorithm.HS256, tokenSignKey)
                .compact();

        System.out.println(token);
    }

    @Test
    public void testGetUserInfo(){
        String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJqYXNvbi11c2VyIiwiaXNzIjoiamFzb24iLCJhdWQiOiJqYXNvbiIsImlhdCI6MTYzNjExODMxOCwiZXhwIjoxNjM2MTE4NjE4LCJuYmYiOjE2MzYxMTgzMzgsImp0aSI6ImE2NTIxMzA0LTMwNjYtNGY3ZC05ZDk2LTJmNmE5ZWYwZDQ2NCIsIm5pY2tuYW1lIjoiemhvbmdsaSIsImF2YXRhciI6IjEuanBnIn0.-e_kTvda_a8QqiF3D_vkPbgPcVP0IsPCHaHZq8FA-I8";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

        String subject = claims.getSubject();
        String issuer = claims.getIssuer();
        String audience = claims.getAudience();
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        Date notBefore = claims.getNotBefore();
        String id = claims.getId();

        System.out.println(subject);
        System.out.println(issuer);
        System.out.println(audience);
        System.out.println(issuedAt);
        System.out.println(expiration);
        System.out.println(notBefore);
        System.out.println(id);;

        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");

        System.out.println(nickname);
        System.out.println(avatar);
    }
}
