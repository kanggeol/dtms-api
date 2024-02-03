package com.dailystudy.dtmsapi.service;

import com.dailystudy.dtmsapi.dto.LoginDto;
import com.dailystudy.dtmsapi.dto.UserDto;
import com.dailystudy.dtmsapi.exception.LoginFailedException;
import com.dailystudy.dtmsapi.exception.UserNotFoundException;
import com.dailystudy.dtmsapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //사용자 정보 가져오는 로직
        log.info("사용자 로드 시도: {}", userId);

        UserDetails userDetails = userMapper.findByUserId(userId)
                .map(user -> addAuthorities(user))
                .orElseThrow(() -> new UserNotFoundException(userId + "을 찾을 수 없습니다."));

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            log.info("부여된 권한: " + authority.getAuthority());
        }
        return userDetails;
    }

    private UserDto addAuthorities(UserDto userDto) {
        // 사용자의 역할에 따라 권한 부여
        if ("birth0114".equals(userDto.getUserId())) {
            userDto.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        } else {
            userDto.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        return userDto;
    }
}