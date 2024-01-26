package com.dailystudy.dtmsapi.base;


import com.dailystudy.dtmsapi.exception.BizException;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Service
public class ServiceBase {
	private final static Logger logger = LoggerFactory.getLogger(ServiceBase.class);

	public ServiceBase() {

	}

	protected void checkArgs(RequestMap req, String... args) {
		for(String arg : args) {
			if(!req.containsKey(arg)) {
				throw new BizException("9002", "잘못된 접근입니다.");
			}
		}
	}
	
	protected void checkArgValue(RequestMap req, String str, String... values) {
		String v = req.get(str, "");
		for(String value : values) {
			if(v.equals(value)) return;
		}
		
		throw new BizException("9002", "잘못된 접근입니다.");
	}
	
	protected void setPaging(RequestMap req) {
		String page_number = "1";
		if(req.containsKey("page_number")) {
			page_number = req.get("page_number")+"";
		}
		
		int page_index = (int) (Double.parseDouble(page_number) - 1);
		req.put("page_start_index", (Global.DEFAULT_PAGE_SIZE * page_index));
		req.put("page_size", Global.DEFAULT_PAGE_SIZE);
	}
	
	protected void setDummyData(String name, ResultMap res) throws IOException {
		
		InputStream in = getClass().getResourceAsStream("dummy/"+name);
		String str = IOUtils.toString(in);
		IOUtils.closeQuietly(in);
		HashMap data = new Gson().fromJson(str, HashMap.class);
		
		res.putAll(data);
	}
}









