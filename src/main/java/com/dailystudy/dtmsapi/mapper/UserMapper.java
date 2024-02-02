package com.dailystudy.dtmsapi.mapper;

import com.dailystudy.dtmsapi.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<UserDto> findByUserId(String id);

    Optional<UserDto> findByUser(String id, String password);
}
