package com.dailystudy.dtmsapi.util;

import com.dailystudy.dtmsapi.exception.BizException;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final Pattern phoneNumberPattern = Pattern.compile("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})$");
    private static final Pattern mobileNumberPattern = Pattern.compile("^(01\\d{1})-?(\\d{3,4})-?(\\d{4})$");
    private static final Pattern datePattern1 = Pattern.compile("^(20\\d{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern datePattern2 = Pattern.compile("^(20\\d{2})/(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern datePattern3 = Pattern.compile("^(20\\d{2})\\.(0[1-9]|1[012])\\.(0[1-9]|[12][0-9]|3[01])$");

	public static boolean isNullOrEmpty(String source) {
		if(source == null) return true;
		if("".equals(source)) return true;
		
		return false;
	}
	
	public static String nvl(String str, String def) {
		if(str == null) return def;
		
		return str;
	}

    /**
     * 양쪽으로 자리수만큼 문자 채우기
     *
     * @param   str         원본 문자열
     * @param   size        총 문자열 사이즈(리턴받을 결과의 문자열 크기)
     * @param   strFillText 원본 문자열 외에 남는 사이즈만큼을 채울 문자
     * @return
     */
    public static String getCPad(String str, int size, String strFillText) {
        int intPadPos = 0;
        for(int i = (str.getBytes()).length; i < size; i++) {
            if(intPadPos == 0) {
                str += strFillText;
                intPadPos = 1;
            } else {
                str = strFillText + str;
                intPadPos = 0;
            }
        }
        return str;
    }


    /**
     * 왼쪽으로 자리수만큼 문자 채우기
     *
     * @param   str         원본 문자열
     * @param   size        총 문자열 사이즈(리턴받을 결과의 문자열 크기)
     * @param   strFillText 원본 문자열 외에 남는 사이즈만큼을 채울 문자
     * @return
     */
    public static String getLPad(String str, int size, String strFillText) {
        for(int i = (str.getBytes()).length; i < size; i++) {
            str = strFillText + str;
        }
        return str;
    }


    /**
     * 오른쪽으로 자리수만큼 문자 채우기
     *
     * @param   str         원본 문자열
     * @param   size        총 문자열 사이즈(리턴받을 결과의 문자열 크기)
     * @param   strFillText 원본 문자열 외에 남는 사이즈만큼을 채울 문자
     * @return
     */
    public static String getRPad(String str, int size, String strFillText) {
        for(int i = (str.getBytes()).length; i < size; i++) {
            str += strFillText;
        }
        return str;
    }

    public static String[] splitPhoneNumber(String phoneNumber) throws BizException {
        try {
            Matcher matcher = phoneNumberPattern.matcher(phoneNumber);
            if(matcher.matches()) {
                //정규식에 적합하면 matcher.group으로 리턴
                return new String[]{ matcher.group(1), matcher.group(2), matcher.group(3)};

            }else{
                //정규식에 적합하지 않으면 substring으로 휴대폰 번호 나누기
                phoneNumber = phoneNumber.replace("-", "");

                String str1 = phoneNumber.substring(0, 3);
                String str2 = phoneNumber.substring(3, 7);
                String str3 = phoneNumber.substring(7, 11);

                return new String[]{ str1, str2, str3};
            }
        } catch (Exception e) {
            throw new BizException("잘못된 전화번호 형식입니다.");
        }
    }

    public static String leftTrim(String text) {
        return leftTrim(text, "\\s");
    }

    public static String leftTrim(String text, String target) {
        return text.replaceAll("^" + target + "+", "");
    }

    public static String removeLeftZero(String text) {
        return text.replaceFirst("^0+(?!$)", "");
    }

    public static String convertPriceFormat(int price) {
        DecimalFormat df = new DecimalFormat("###,###");

        return df.format(price);
    }

    public static boolean isValidDateFormat(String date) throws Exception {
        return isValidDateFormat(date,  "yyyy-MM-dd");
    }

    public static boolean isValidDateFormat(String date, String format) throws Exception {
        if(format.equals("yyyy-MM-dd")) {
            return datePattern1.matcher(date).matches();
        } else if(format.equals("yyyy/MM/dd")) {
            return datePattern2.matcher(date).matches();
        } else if(format.equals("yyyy.MM.dd")) {
            return datePattern3.matcher(date).matches();
        } else {
            throw new BizException("검증하려는 날짜 형식이 패턴이 유효하지 않습니다.");
        }
    }

    public static boolean isValidPhoneNumber(String phoneNumber) throws Exception {
        Matcher matcher = phoneNumberPattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidMobileNumber(String phoneNumber) throws Exception {
        Matcher matcher = mobileNumberPattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static String subStrBytes(String source, int cutLength) {
        if(StringUtils.isEmpty(source)) {
            return "";
        }

        source = source.trim();
        if(source.getBytes().length <= cutLength) {
            return source;
        } else {
            StringBuffer sb = new StringBuffer(cutLength);
            int cnt = 0;
            for(char ch : source.toCharArray()){
                cnt += String.valueOf(ch).getBytes().length;
                if(cnt > cutLength) break;
                sb.append(ch);
            }
            return sb.toString();
        }
    }

    public static String rawUrlEncode(String text) throws Exception{
    	try {
    		return URLEncoder.encode(text, "UTF-8").replace("*", "%2A").replace("+", "%20").replace("%7E", "~");
    	} catch (UnsupportedEncodingException e) {
    		throw new BizException("rawUrlEncode 실패");
    	}
    }
}
