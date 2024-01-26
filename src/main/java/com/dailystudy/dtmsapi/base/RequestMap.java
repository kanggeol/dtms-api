package com.dailystudy.dtmsapi.base;

import com.dailystudy.dtmsapi.exception.BizException;
import com.dailystudy.dtmsapi.model.Employee;
import com.dailystudy.dtmsapi.util.SqlSafeUtil;
import com.dailystudy.dtmsapi.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class RequestMap extends DbMap{
	private static final Logger logger = LoggerFactory.getLogger(RequestMap.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 2534688570550397437L;
	private static Gson _gson = new GsonBuilder().registerTypeAdapter(DbMap.class, new MapDeserializer()).create();

	private HttpServletRequest _request;

	public RequestMap() {

	}

	public RequestMap(Map<? extends String, ? extends Object> map){
		super(map);
	}

	public static RequestMap create(HttpServletRequest request) throws Exception {
		return create(request, true);
	}

	public static RequestMap create(HttpServletRequest request, boolean isAuthCheck) throws Exception {
		String contentType = StringUtil.nvl(request.getContentType(), "");
		if(contentType.startsWith("application/json")) {
			String message;
			try {
				message = org.apache.commons.io.IOUtils.toString(request.getInputStream(), "utf-8");
			}
			catch(IOException ioe) {
				throw new BizException("9901", "network error");
			}

			RequestMap map = new RequestMap(_gson.fromJson(message, new TypeToken<DbMap>(){}.getType()));
			if(isAuthCheck) {
				setDefaultValues(request, map);
			}
			checkSqlInjectionSafe(map);

			//multipart/form-data;
			map._request = request;

			return map;

		} else if(contentType.startsWith("multipart/form-data")) {
			RequestMap map = new RequestMap();
			Enumeration en = request.getParameterNames();
			while(en.hasMoreElements()) {
				String key = en.nextElement() + "";
				String value = ServletRequestUtils.getStringParameter(request, key);
				map.put(key, value);
			}

			if(isAuthCheck) {
				setDefaultValues(request, map);
			}

			logger.info("file request ={}", map);

			checkSqlInjectionSafe(map);

			map._request = request;

			return map;

		} else {
			if(!Global.IS_DEV) {
				throw new Exception("인증되지 않았습니다.");
			}
			// debuging
			RequestMap map = new RequestMap();
			Enumeration en = request.getParameterNames();
			while(en.hasMoreElements()) {
				String key = en.nextElement() + "";
				String value = ServletRequestUtils.getStringParameter(request, key);
				map.put(key, value);
			}

			if(isAuthCheck) {
				setDefaultValues(request, map);
			}

			checkSqlInjectionSafe(map);

			map._request = request;

			return map;
		}
	}

	private static void setDefaultValues(HttpServletRequest request, RequestMap map) {
		for(String item : Global.DISABLE_REQUEST_PARAMETERS) {
			if(map.containsKey(item)) {
				throw new BizException(ResultType.requestError2);
			}
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && request.getRequestURI().startsWith("/admin/")) {
			Employee memberDetails = (Employee)authentication.getPrincipal();
			map.put("authMemberNumber", memberDetails.getPrimaryKey());
			map.put("authMemberId", memberDetails.getId());
			map.put("authMemberName", memberDetails.getName());
			map.put("authUserIdentifierId", memberDetails.getIdentifierId());
			map.put("dbPrefixName", memberDetails.getDbPrefixName());
			if(map.get("dbPrefixName", "error").equals("master")) {
				map.put("dbPrefixName", "");
			}

			map.put("authUserType", "직원");
			map.put("authMemberType", memberDetails.getType());
			map.put("authMemberCompany", memberDetails.getCompany());
			map.put("authMemberAcademyCode", memberDetails.getAcademy());
			map.put("authMemberAcademyName", memberDetails.getAcademyName());
			map.put("authMemberSelectedAcademy", memberDetails.getSelectedAcademy());
			map.put("authMemberGrade", memberDetails.getGrade());
			map.put("authMemberDepartment", memberDetails.getDepartment());
			map.put("authMemberSmsDatabaseName", memberDetails.getSmsDatabaseName());
			map.put("authMemberAcademyPhone", memberDetails.getAcademyPhone());
			map.put("authMemberAcademyAliasName", memberDetails.getAcademyAliasName());
			map.put("authMemberAcademyCategoryCode", memberDetails.getAcademyCategoryCode());
			map.put("authMemberAcademyCategoryName", memberDetails.getAcademyCategoryName());
			map.put("authMemberAssignClassDecisionDay", memberDetails.getAssignClassDecisionDay());
			map.put("authMemberAcademyCategoryPhone", memberDetails.getAcademyCategoryPhone());
			map.put("authMemberAcademyUseNormalMessage", memberDetails.getAcademyUseNormalMessage());
			map.put("authMemberAcademyUseKakaoMessage", memberDetails.getAcademyUseKakaoMessage());
			map.put("authMemberAcademyWebsite", memberDetails.getAcademyWebsite());
			map.put("authMemberPrivilege", memberDetails.getPrivilege());
			map.put("authMemberAcademyCompanyId", memberDetails.getAcademyCompanyId());
		}


		String remoteIP = request.getHeader("X-FORWARDED-FOR");
		map.put("remoteIP", remoteIP == null? request.getRemoteAddr() : remoteIP);
		map.put("httpHost", "http://"+request.getServerName());
		map.put("httpHostContext", "http://"+request.getServerName() + request.getContextPath());

		if(Global.IS_DEV) {
			map.put("isDevServer", "Y");
		}
	}

	private static void checkSqlInjectionSafe(RequestMap map) throws Exception {
		if(map.containsKey("searchConditionList")) {
			List<DbMap> searchConditionList = (List) map.get("searchConditionList");

			for (DbMap map2 : searchConditionList) {
				if (!SqlSafeUtil.isSqlInjectionSafe(map2.get("id", ""))) {
					throw new BizException();
				}
			}
		}

		if(map.containsKey("pageOrderByList")) {
			List<DbMap> pageOrderByList = (List) map.get("pageOrderByList");
			if(pageOrderByList.size() > 0) {
				List<String> orderByList = new ArrayList<>();
				for(DbMap orderBy: pageOrderByList) {
					if(!orderBy.get("pageOrder", "").equals("asc") && !orderBy.get("pageOrder", "").equals("desc")) {
						throw new BizException();
					}

					if(!SqlSafeUtil.isSqlInjectionSafe(orderBy.get("pageOrderBy", ""))) {
						throw new BizException();
					}

					if(orderBy.get("isAutoOrderBy", "Y").equals("Y")) {
						orderByList.add(orderBy.get("pageOrderBy", "") + " " + orderBy.get("pageOrder", ""));
					}
				}

				String pageOrderBy = String.join(",", orderByList);
				map.put("pageOrderBy", pageOrderBy);
			}
		}
	}

	public RequestMap extractMustHaveMap() throws Exception {
		if(!this.containsKey("dbPrefixName")) {
			throw new BizException("dbPrefixName 키가 없습니다.");
		}
		if(!this.containsKey("authMemberNumber")) {
			throw new BizException("authMemberNumber 키가 없습니다.");
		}
		if(!this.containsKey("authMemberSelectedAcademy")) {
			throw new BizException("authMemberSelectedAcademy 키가 없습니다.");
		}
		if(!this.containsKey("authMemberAcademyCode")) {
			throw new BizException("authMemberAcademyCode 키가 없습니다.");
		}
		if(!this.containsKey("authMemberId")) {
			throw new BizException("authMemberId 키가 없습니다.");
		}

		RequestMap requestMap = new RequestMap();
		requestMap.put("dbPrefixName", this.get("dbPrefixName", "error"));
		requestMap.put("authMemberNumber", this.get("authMemberNumber", 0));
		requestMap.put("authMemberId", this.get("authMemberId", ""));
		requestMap.put("authMemberSelectedAcademy", this.get("authMemberSelectedAcademy", ""));
		requestMap.put("authMemberAcademyCode", this.get("authMemberAcademyCode", ""));

		return requestMap;
	}

	public String dbPrefixName()  {
		return this.get("dbPrefixName", "error");
	}

	public boolean isMasterDB()  {
		return dbPrefixName().isEmpty();
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public int authMemberNumber() throws Exception {
		return this.get("authMemberNumber", 0);
	}

	public String authMemberSelectedAcademy() throws Exception {
		return this.get("authMemberSelectedAcademy", "");
	}

	public String authMemberAcademyCode() throws Exception {
		return this.get("authMemberAcademyCode", "");
	}

	public String remoteIP() {
		return this.get("remoteIP", "");
	}

	public String authMemberId() throws Exception {
		return this.get("authMemberId", "");
	}

	public String authMemberAcademyPhone() {
		return this.get("authMemberAcademyPhone", "");
	}
}