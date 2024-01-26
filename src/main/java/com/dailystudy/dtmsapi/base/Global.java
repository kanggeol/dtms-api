package com.dailystudy.dtmsapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Global {
	private static final Logger logger = LoggerFactory.getLogger(Global.class);
	public static boolean IS_DEV;
	public final static int DEFAULT_PAGE_SIZE = 30;
	public final static String[] DISABLE_REQUEST_PARAMETERS = {"authMemberNumber","authMemberId","authMemberName","authUserIdentifierId","dbPrefixName","authUserType","authMemberType","authMemberCompany","authMemberAcademyCode","authMemberAcademyName","authMemberSelectedAcademy","authMemberGrade","authMemberDepartment","authMemberSmsDatabaseName","authMemberAcademyPhone","authMemberSelectedAcademyPhone","authMemberAcademyAliasName","authMemberAcademyCategoryCode","authMemberAcademyCategoryName","authMemberAssignClassDecisionDay", "pageOrderBy"};
	public final static String PHP_PASSWORD_ENCRYPT_URL = "https://www.dailystudy.com/GetEncryptCode.php";
	public final static String MANGOPAY_147_STORE_CODE = "SDRJV494614500101448";
	public final static String MANGOPAY_147_COM_API_KEY = "kdMMyhprHAZMjOMEMFAnjBSijyYkNtZwcjcKHwwu";
	public final static String MANGOPAY_147_CAT_ID = "4946145001";
	public final static String MANGOPAY_RDS_STORE_CODE = "SDJLB389325800102650";
	public final static String MANGOPAY_RDS_COM_API_KEY = "kdMMyhprHAZMjOMEMFAnjBSijyYkNtZwcjcKHwwu";
	public final static String MANGOPAY_RDS_CAT_ID = "3893258001";
	public final static String MANGOPAY_DEEPTHOUGHT_STORE_CODE = "SDLNK368663200101270";
	public final static String MANGOPAY_DEEPTHOUGHT_COM_API_KEY = "kdMMyhprHAZMjOMEMFAnjBSijyYkNtZwcjcKHwwu";
	public final static String MANGOPAY_DEEPTHOUGHT_CAT_ID = "3686632001";
	public final static String MANGOPAY_DEV_STORE_CODE = "SDABD239330000100012";
	public final static String MANGOPAY_DEV_COM_API_KEY = "AhsnIwiSvjOjLTgFNdkm2dmRIlKatZdahdfhjabc";
	public final static String MANGOPAY_DEV_CAT_ID = "2393300001";
	public final static String MANGOPAY_PROD_API_URL = "https://web.mangopay.co.kr/api/comps/";
	public final static String MANGOPAY_DEV_API_URL = "https://dev.mangopay.co.kr/api/comps/";
	public final static String[] MASTER_COMPANY_ACADEMY_CATEGORIES = {"master", "song", "seocho", "gwangjin"};

}
