package com.dailystudy.dtmsapi.exception;

import com.dailystudy.dtmsapi.base.ResultType;

public class BizException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6650410639570678979L;
	
	protected ResultType result;
    protected String resultCode;
	
	public BizException() {
		super(ResultType.unknowError.message());
		this.result = ResultType.unknowError;
		this.resultCode = "9999";
	}
	
	public BizException(String message, Throwable t) {
		super(message, t);
		this.result = ResultType.messageError;
		this.resultCode = this.result.code();
	}
	
	public BizException(String message) {
		this(message, (Throwable)null);
	}

	public BizException(String resultCode, String message) {
		this(message, (Throwable)null);
		this.resultCode = resultCode;
	}

	public BizException(ResultType result, String message, Throwable t) {
		super(message, t);
		this.result = result;
		this.resultCode = this.result.code();
	}
	
	public BizException(ResultType result, String message) {
		this(result, message, (Throwable)null);
	}
	
	public BizException(ResultType result) {
		this(result, result.message(), (Throwable)null);
	}
	
	public ResultType result() {
		return this.result;
	}
	
	public String resultCode() {
		return this.resultCode;
	}
}
