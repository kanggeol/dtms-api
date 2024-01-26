package com.dailystudy.dtmsapi.service;

import com.dailystudy.dtmsapi.base.*;
import com.dailystudy.dtmsapi.exception.BizException;
import com.dailystudy.dtmsapi.mapper.EmployeeMapper;
import com.dailystudy.dtmsapi.mapper.LoginMapper;
import com.dailystudy.dtmsapi.security.JwtTokenProvider;
import com.dailystudy.dtmsapi.util.DateUtil;
import com.dailystudy.dtmsapi.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Common service.
 */
@Service
@RequiredArgsConstructor
public class LoginService extends ServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final EmployeeMapper employeeMapper;
    private final LoginMapper loginMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> notAccessibleDepartment = new HashSet<>(Arrays.asList("출판", "마음동행"));

    public ResultMap login(HttpServletRequest request) throws Exception {
        RequestMap requestMap = RequestMap.create(request);
        LogManager.requestLog(this.getClass(), "login", requestMap);

        if(requestMap.get("userId", "").isEmpty() || requestMap.get("password", "").isEmpty()) {
            throw new BizException("필수값이 없습니다.");
        }

        DbMap userIdentifier = employeeMapper.userIdentifier(requestMap);
        if(userIdentifier == null || userIdentifier.get("dbPrefixName", "").isEmpty()) {
           throw new BizException("일치하는 아이디가 없습니다.");
        }

        String dbPrefixName = userIdentifier.get("dbPrefixName", "");
        requestMap.put("memberId", userIdentifier.get("userId", ""));
        requestMap.put("dbPrefixName", dbPrefixName.equals("master")? "" : dbPrefixName);

        //TODO 추후 마지막 비밀번호 변경 일자를 체크하여 제한시간이 지난 경우 로그인 못 하게 변경
        DbMap user = loginMapper.employee(requestMap);
        if(user == null || user.get("memberId", "").isEmpty()) {
            throw new BizException("일치하는 아이디가 없거나 비밀번호가 틀렸습니다.");
        }

        if(dbPrefixName.equals("master") && notAccessibleDepartment.contains(user.get("department", ""))) {
            throw new BizException("로그인 권한이 없습니다.");
        }

        DbMap result = new DbMap();
        result.put("accessToken", jwtTokenProvider.createJwtAccessToken(user.get("memberId", "")));

        String refreshToken = EncryptUtil.makeSHA256Key(userIdentifier.get("id", "") + "_" + DateUtil.getCurrentTimestamp());
        DbMap map = new DbMap();
        map.put("token", refreshToken);
        map.put("memberId", user.get("memberId", ""));
        map.put("ip", requestMap.remoteIP());
        if(loginMapper.insertRefreshToken(map) == 0) {
            logger.error("insertRefreshToken 실패 = {}", map);
            throw new BizException();
        }

        result.put("refreshToken", refreshToken);

        ResultMap resultMap = ResultMap.create();
        resultMap.put("resultData", result);

        LogManager.responseLog(this.getClass(), "login", resultMap);

        return resultMap;
    }

    public ResultMap createAccessToken(HttpServletRequest request) throws Exception {
        RequestMap requestMap = RequestMap.create(request);
        LogManager.requestLog(this.getClass(), "createAccessToken", requestMap);

        if(requestMap.get("refreshToken", "").isEmpty()) {
            throw new BizException("잘못된 접근입니다.");
        }

        String accessToken = request.getHeader("Authorization");
        if(accessToken == null || accessToken.isEmpty()) {
            throw new BizException("잘못된 접근입니다.");
        }

        DbMap map = new DbMap();
        map.put("token", requestMap.get("refreshToken", ""));
        DbMap tokenMap = loginMapper.refreshToken(map);
        if(tokenMap == null) {
            throw new BizException(ResultType.notExistTokenError);
        }

        ResultMap resultMap = ResultMap.create();
        resultMap.put("resultData", jwtTokenProvider.createJwtAccessToken(tokenMap.get("memberId", "")));
        LogManager.responseLog(this.getClass(), "createAccessToken", resultMap);

        return resultMap;
    }
}
