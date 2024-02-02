package com.dailystudy.dtmsapi.controller;

import com.dailystudy.dtmsapi.dto.LoginDto;
import com.dailystudy.dtmsapi.dto.UserDto;
import com.dailystudy.dtmsapi.dto.response.BaseResponse;
import com.dailystudy.dtmsapi.dto.response.SingleDataResponse;
import com.dailystudy.dtmsapi.exception.DuplicatedmemberNameException;
import com.dailystudy.dtmsapi.exception.LoginFailedException;
import com.dailystudy.dtmsapi.exception.UserNotFoundException;
import com.dailystudy.dtmsapi.service.ResponseService;
import com.dailystudy.dtmsapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody(required = true) LoginDto loginDto) {
        ResponseEntity responseEntity = null;
        try {
            String token = userService.login(loginDto);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);

            SingleDataResponse<String> response = responseService.getSingleDataResponse(true, "로그인 성공", token);

            responseEntity = ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(response);
        } catch (LoginFailedException exception) {
            log.debug(exception.getMessage());
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }

    @GetMapping("/members")
    public ResponseEntity findUserByuserId(final Authentication authentication) {
        ResponseEntity responseEntity = null;
        try {
            String userId = ((UserDto) authentication.getPrincipal()).getUserId();
            UserDto findUser = userService.findByUserId(userId);

            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException exception) {
            log.debug(exception.getMessage());
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }
}