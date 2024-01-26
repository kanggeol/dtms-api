package com.dailystudy.dtmsapi.security;

import com.dailystudy.dtmsapi.base.ResultType;
import com.dailystudy.dtmsapi.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, BizException {
        if(request.getRequestURI().startsWith("/admin/")) {
            String token = jwtTokenProvider.resolveJwtToken(request);
            if(token == null) {
                throw new BizException(ResultType.notExistTokenError);
            }

            token = token.replace("Bearer ", "");
            JwtTokenProvider.TokenValidResultType tokenValidResultType = jwtTokenProvider.validateToken(token);
            if(tokenValidResultType == JwtTokenProvider.TokenValidResultType.needLogin) {
                throw new BizException(ResultType.notExistTokenError);
            }

            if(tokenValidResultType == JwtTokenProvider.TokenValidResultType.needNewToken) {
                throw new BizException(ResultType.expiredAccessTokenError);
            }

            if(tokenValidResultType == JwtTokenProvider.TokenValidResultType.notExpired) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
