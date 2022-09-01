package com.ouni.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ouni.domain.Vo.UserVo;
import org.springframework.stereotype.Service;
import java.util.Calendar;


@Service
public  class JWTUtil {
    public static String getToken(UserVo uvo){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1); //默认令牌过期时间7天
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("open_id", uvo.getOpenId())
                .withClaim("session_key", uvo.getSessionKey());
        return builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256("jml"));
    }

    public static DecodedJWT verifyToken(String token) {
        if (token==null){
            return null;
        }
        JWTVerifier build = JWT.require(Algorithm.HMAC256("jml")).build();
        return build.verify(token);
    }
}

