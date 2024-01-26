package com.dailystudy.dtmsapi.exception;

public class ArgsException extends BizException  {

	private static final long serialVersionUID = 2225212098917599281L;
	
	private String _argsName;
	
	public ArgsException(String argsName) {
		super("4003", "입력변수가 잘못되었습니다. arg=" + argsName);
		
		_argsName = argsName;
	}
	
	public String argsName() {
		return _argsName;
	}
	
}