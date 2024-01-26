package com.dailystudy.dtmsapi.base;

import com.dailystudy.dtmsapi.exception.BizException;

public class ResultMap extends DbMap{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7247523019191457251L;
	
	private ResultMap() {
		setResult(ResultType.success);
	}

    public void setResult(ResultType type) {
		this.put("resultCode", type.code());
		this.put("resultMessage", type.message());
	}
	
	public void setResult(ResultType type, String message) {
		this.put("resultCode", type.code());
		this.put("resultMessage", message);
	}

	public void setResult(String type, String message) {
		this.put("resultCode", type);
		this.put("resultMessage", message);
	}

	public void setResultMessage(String message) {
		this.put("resultMessage", message);
	}
	
	public static ResultMap create() {
		ResultMap result = new ResultMap();
		return result;
	}

    public static ResultMap create(DbMap map) {
        ResultMap result = new ResultMap();
        result.putAll(map);
        return result;
    }

    public static ResultMap create(Exception e) {
		ResultMap result = create();
		if(e instanceof BizException) {
			ResultType resultType = ((BizException)e).result();
			result.setResult(resultType, e.getMessage());

			String resultCode = ((BizException)e).resultCode();
			if(resultCode != null) {
				result.setResult(resultCode, e.getMessage());
			}
		}
		else {
			result.setResult(ResultType.unknowError, "관리자에게 문의하세요");
		}
		
		return result;
	}

	public void setResultData(Object value) {
		this.put("resultData", value);
	}
	
}
