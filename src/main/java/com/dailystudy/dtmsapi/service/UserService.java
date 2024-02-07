package com.dailystudy.dtmsapi.service;

import com.dailystudy.dtmsapi.dto.LoginDto;
import com.dailystudy.dtmsapi.dto.UserDto;
import com.dailystudy.dtmsapi.exception.LoginFailedException;
import com.dailystudy.dtmsapi.exception.UserNotFoundException;
import com.dailystudy.dtmsapi.mapper.UserMapper;
import com.dailystudy.dtmsapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginDto loginDto) {
        UserDto userDto = userMapper.findByUser(loginDto.getId(), loginDto.getPassword())
                .orElseThrow(() -> new LoginFailedException("일치하는 아이디가 없거나 비밀번호가 틀렸습니다."));

        return jwtTokenProvider.createToken(userDto.getUserId(), userDto.getRole());
    }

    public UserDto findByUserId(String userId) {
        return userMapper.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    }

}