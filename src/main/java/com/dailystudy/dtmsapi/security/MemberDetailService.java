package com.dailystudy.dtmsapi.security;

import com.dailystudy.dtmsapi.base.DbMap;
import com.dailystudy.dtmsapi.mapper.EmployeeMapper;
import com.dailystudy.dtmsapi.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberDetailService implements UserDetailsService{
    private static final Logger logger = LoggerFactory.getLogger(MemberDetailService.class);

    private final EmployeeMapper employeeMapper;
//    private final StudentMapper studentMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUsername(String userId, HttpServletRequest request) throws UsernameNotFoundException {
        String remoteIP = request.getHeader("X-FORWARDED-FOR");
        if (remoteIP == null) remoteIP = request.getRemoteAddr();

        DbMap map = new DbMap();
        map.put("userId", userId);
        DbMap userIdentifier = employeeMapper.userIdentifier(map);
        if (userIdentifier == null || userIdentifier.get("dbPrefixName") == null || userIdentifier.get("dbPrefixName").toString().isEmpty()) {
            throw new UsernameNotFoundException("일치하는 아이디가 없습니다.");
        }

        String dbPrefixName = "master".equals(userIdentifier.get("dbPrefixName")) ? "" : userIdentifier.get("dbPrefixName").toString();

        map = new DbMap();
        map.put("memberId", Objects.toString(userIdentifier.get("userId"), ""));
        map.put("dbPrefixName", dbPrefixName);

        DbMap user = employeeMapper.employee(map);
        if (user == null || user.isEmpty()) {
            throw new UsernameNotFoundException("일치하는 아이디가 없습니다.");
        }

        String selectedAcademy = Objects.toString(user.get("academyCode"), "");
        if(request.getHeader("selectedAcademy") != null && !request.getHeader("selectedAcademy").isEmpty()) {
            selectedAcademy = request.getHeader("selectedAcademy");
        }

        Employee memberDetails = new Employee();

        memberDetails.setPrimaryKey(user.get("memberNumber", "0"));
        memberDetails.setDepartment(user.get("department", ""));
        memberDetails.setGrade(user.get("grade", ""));
        memberDetails.setType(user.get("memberType", ""));
        memberDetails.setDbPrefixName(userIdentifier.get("dbPrefixName", "error"));
        memberDetails.setId(userIdentifier.get("userId", ""));
        memberDetails.setName(user.get("memberName", ""));
        memberDetails.setPassword("");
        memberDetails.setAcademy(user.get("academyCode", ""));
        memberDetails.setAcademyName(user.get("academyName", ""));
        memberDetails.setCompany(user.get("academyCode", ""));
        memberDetails.setSelectedAcademy(selectedAcademy);
        memberDetails.setSmsDatabaseName(user.get("smsDatabaseName", ""));
        memberDetails.setAcademyPhone(user.get("academyPhone", ""));
        memberDetails.setAcademyAliasName(user.get("aliasName", ""));
        memberDetails.setAcademyCategoryCode(user.get("academyCategoryCode", ""));
        memberDetails.setAcademyCategoryName(user.get("academyCategoryName", ""));
        memberDetails.setAssignClassDecisionDay(user.get("assignClassDecisionDay", ""));
        memberDetails.setAcademyCategoryPhone(user.get("academyCategoryPhone", ""));
        memberDetails.setAcademyUseNormalMessage(user.get("useNormalMessage", "N"));
        memberDetails.setAcademyUseKakaoMessage(user.get("useKakaoMessage", "N"));
        memberDetails.setAcademyWebsite(user.get("website", ""));
        memberDetails.setAcademyCompanyId(user.get("academyCompanyId", 0));

        map = new DbMap();
        map.put("dbPrefixName", dbPrefixName);
        map.put("privilegeCategory", "슈퍼관리자");
        map.put("authMemberNumber", user.get("memberNumber", 0));
        memberDetails.setPrivilege(employeeMapper.memberExtraPrivilegeCount(map) > 0? "슈퍼관리자" : "일반");

        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(authority);

        map = new DbMap();
        map.put("dbPrefixName", dbPrefixName);
        map.put("authMemberNumber", user.get("memberNumber", 0));
        map.put("authMemberSelectedAcademy", selectedAcademy);
        if(employeeMapper.accessibleAcademy(map) == 0) {
            throw new UsernameNotFoundException("학원 접근 권한이 없습니다.");
        }

        //1. 메뉴에 접근할 수 있는 권한이 반드시 있어야 하는 메뉴(needPrivilege = 'Y' 메뉴)는 접근한 멤버에게 요청한 메뉴에 대한 권한이 반드시 있어야 한다.
        //2. 메뉴에 접근할 수 있는 권한이 꼭 필요하지 않은 메뉴(needPrivilege = 'N' 메뉴)는 접근한 멤버에게 메뉴 권한이 없어도 접근 가능하지만 referer 없이 직접적으로 접근하는걸을 허용하지 않는다. 단, 해당 메뉴에 대한 권한이 접근한 멤버에게 있다면 referer가 없어도 접근 가능하다.
        map = new DbMap();
        map.put("uri", request.getRequestURI().replace("/admin", ""));
        map.put("selectedAcademy", selectedAcademy);
        map.put("memberNumber", memberDetails.getPrimaryKey());
//        DbMap menuPrivilegeMap = employeeMapper.accessibleMenu(map);
//        if(menuPrivilegeMap != null && (request.getRequestURI().equals("/admin/main/index") || !menuPrivilegeMap.get("memberNumber", "").isEmpty() || !request.getHeader("referer").isEmpty())) {
//            authority = new SimpleGrantedAuthority("ROLE_ACCESSIBLE");
//            authorities.add(authority);
//        }

        authority = new SimpleGrantedAuthority("ROLE_ACCESSIBLE");
        authorities.add(authority);
        memberDetails.setAuthorities(authorities);

        return memberDetails;
    }

//    public UserDetails loadUserByReportUser(DbMap reportMap, HttpServletRequest request) throws UsernameNotFoundException {
//        String id = request.getParameter("id");
//        ReportDetails reportDetails = new ReportDetails();
//
//        if(reportMap == null || id == null || !reportMap.get("id", "").equals(id)) {
//            throw new UsernameNotFoundException("잘못된 접근입니다");
//        }
//
//        DbMap studentIdentifier = studentMapper.studentIdentifier(reportMap);
//        if(studentIdentifier == null) {
//            throw new UsernameNotFoundException("일치하는 학생이 없습니다");
//        }
//
//        reportDetails.setPrimaryKey(reportMap.get("id", 0));
//        reportDetails.setDbPrefixName(studentIdentifier.get("dbPrefixName", "error"));
//        reportDetails.setStudentNumber(reportMap.get("studentNumber", 0));
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
//        authorities.add(authority);
//
//        authority = new SimpleGrantedAuthority("ROLE_ACCESSIBLE");
//        authorities.add(authority);
//
//        reportDetails.setAuthorities(authorities);
//
//        return reportDetails;
//    }
}
