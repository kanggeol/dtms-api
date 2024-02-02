package com.dailystudy.dtmsapi.service;

import com.dailystudy.dtmsapi.dto.LoginDto;
import com.dailystudy.dtmsapi.dto.UserDto;
import com.dailystudy.dtmsapi.exception.LoginFailedException;
import com.dailystudy.dtmsapi.exception.UserNotFoundException;
import com.dailystudy.dtmsapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userMapper.findByUserId(userId)
                .map(user -> addAuthorities(user))
                .orElseThrow(() -> new UserNotFoundException(userId + "> 찾을 수 없습니다."));
    }

    private UserDto addAuthorities(UserDto userDto) {
//        userDto.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(userDto.getRole())));
        userDto.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("USER")));

        return userDto;
    }
}