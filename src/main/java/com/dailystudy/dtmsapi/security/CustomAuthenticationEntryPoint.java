package com.dailystudy.dtmsapi.security;

import com.dailystudy.dtmsapi.base.ResultMap;
import com.dailystudy.dtmsapi.base.ResultType;
import com.dailystudy.dtmsapi.exception.BizException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ResultType resultType = ResultType.notExistTokenError;
        if(request.getRequestURI().startsWith("/report/")) {
            resultType = ResultType.noPrivilegeError;
        }

        ResultMap resultMap = ResultMap.create(new BizException(resultType));
        String json = new Gson().toJson(resultMap);
        response.setStatus(200);
        response.getWriter().println(json);
    }
}
