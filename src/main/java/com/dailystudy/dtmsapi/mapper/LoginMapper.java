package com.dailystudy.dtmsapi.mapper;

import com.dailystudy.dtmsapi.base.DbMap;

public interface LoginMapper {
    DbMap employee(DbMap map);
    int insertRefreshToken(DbMap map);
    DbMap refreshToken(DbMap map);
}
