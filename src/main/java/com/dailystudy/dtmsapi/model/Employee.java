package com.dailystudy.dtmsapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements UserDetails {
    private static final long serialVersionUID = 3157712863790301250L;

    private String name;
    private String password;
    private String id;
    private String identifierId;
    private String dbPrefixName;
    private String type;
    private String company;
    private String academy;
    private String academyName;
    private String selectedAcademy;
    private String selectedAcademyPhone;
    private String selectedAcademyCategoryCode;
    private String primaryKey;
    private String grade;
    private String department;
    private String smsDatabaseName; //sms 메시지 디비명
    private String academyPhone; //소속학원 전화번호
    private String academyAliasName; //소속학원명 별칭
    private String academyCategoryCode; //소속학원 카테고리 코드
    private String academyCategoryName; //소속학원 카테고리명
    private String assignClassDecisionDay; //소속학원 분반가능 기간
    private String academyCategoryPhone; //소속학원 카테고리 대표 번호
    private String academyUseNormalMessage; //일반문자 사용여부
    private String academyUseKakaoMessage; //카카오톡 사용여부
    private String privilege; //멤버 권한 유형
    private String academyWebsite; //학원 홈페이지 종류
    private int academyCompanyId; //학원 법인 pk

    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return null;
    }
}
