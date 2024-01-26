package com.dailystudy.dtmsapi.security;

import com.dailystudy.dtmsapi.base.ResultMap;
import com.dailystudy.dtmsapi.base.ResultType;
import com.dailystudy.dtmsapi.exception.BizException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        boolean isAdmin = false;
        boolean isAccessible = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for(GrantedAuthority grantedAuthority: authentication.getAuthorities()) {
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
            } else if(grantedAuthority.getAuthority().equals("ROLE_ACCESSIBLE")) {
                isAccessible = true;
            }
        }

        ResultType resultType = ResultType.notExistTokenError;
        if(!isAdmin && request.getRequestURI().startsWith("/report/")) {
            resultType = ResultType.noPrivilegeError;
        } else if(!isAdmin) {
            resultType = ResultType.notExistTokenError;
        } else if(!isAccessible) {
            resultType = ResultType.noPrivilegeError;
        }

        ResultMap resultMap = ResultMap.create(new BizException(resultType));
        String json = new Gson().toJson(resultMap);
        response.setStatus(200);
        response.getWriter().println(json);
    }
}
