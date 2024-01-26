package com.dailystudy.dtmsapi.base;

public enum ResultType {
	success("0000", ""),
	requestError("4000", "잘못된 요청입니다."),
	requestError2("4001", "잘못된 요청입니다."),
	notExistTokenError("4100", "로그인 후 이용해주세요."),
	expiredAccessTokenError("4101", "만료된 토큰입니다."),
	badAcessTokenError("4102", "정상적인 토큰이 아닙니다."),
	noPrivilegeError("4300", "접근권한이 없습니다."),
	messageError("9000", ""),
	unknowError("9999", "관리자에게 문의하세요.");
	
	private String code;
	private String message;

	ResultType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return this.code;
	}
	public String message() {
		return this.message;
	}
	
}
