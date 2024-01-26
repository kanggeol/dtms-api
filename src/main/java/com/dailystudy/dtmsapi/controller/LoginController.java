package com.dailystudy.dtmsapi.controller;

import com.dailystudy.dtmsapi.base.ResultMap;
import com.dailystudy.dtmsapi.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;


    @RequestMapping(value = "login")
    public ResultMap login(HttpServletRequest request) throws Exception {
        return loginService.login(request);
    }

    @RequestMapping(value = "createAccessToken")
    public ResultMap createAccessToken(HttpServletRequest request) throws Exception {
        return loginService.createAccessToken(request);
    }

}
