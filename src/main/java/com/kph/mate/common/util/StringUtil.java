/**
 * String 유틸리티
 */
package com.kph.mate.common.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class StringUtil {

	  /**
	   * 임시 패스워드 생성(12자리)
	 * @param _size 패스워드 크기
	 * @return
	 */
	public static String createRandomPassword(int _size)
	  {
	      //가능한 패스워드 문자
	      String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ1234567890" ;
	      int MAX_PASSWD_LENGTH = _size;
	      String pw = "" ;
	      for ( int i= 0 ; i<MAX_PASSWD_LENGTH; i++ )
	      {
	            Random random = new Random();
	          int index = ( int )( random.nextDouble() *letters.length()) ;
	          pw += letters.substring ( index, index+ 1 ) ;
	      }
	      return pw;
	  }

	 /**
	 * 문자열을 받아서 null,공백 이면 0을 , 숫자이면 정수를 리턴
	 * @author 김한섭
	 * @param str 문자열
	 * @return int
	 */
	public static long ifNullInt(String str)
	{
		int _result = 0;
		if ((str == null) || (str.trim().equals("")) || (str.trim().equals("null")))
			return _result;
		else {
			/*
	        StringBuffer sb = new StringBuffer();
	        String number = "1234567890";

	        for(int i=0; i<str.length(); i++){
	            if(number.indexOf(str.charAt(i)) > -1){
	                sb.append(str.charAt(i));
	            }
	        }
			return Integer.parseInt(sb.toString());
			*/
			return Math.round(Double.parseDouble(StringUtil.getNumberRemoveComma(str)));
		}
	}

	/**
	 *	입력문자열을 BYTE 단위로 잘라준다.
	 *	한글 2Byte, 영문/숫자 1Byte로 계산
	 * @author 김한섭
	 * @param str 문자열
	 * @return str
	 */
	public static String getByteSubstring(String TargetStr,int _begin,int _size )
	{
		String _result = "";
		int _byteLen = 0;

		int _end = _begin + _size - 1;

		if( TargetStr != null ) {
			for( int i=0; i < TargetStr.length() && _byteLen < _end; i++ ) {
				Character code = new Character( TargetStr.charAt( i ) );
				int _code = code.hashCode();

				if( _code > 255 || _code < 0 )
					_byteLen += 2;       // 한글
				else
					_byteLen += 1;       // 영문

				if (_byteLen >= _begin && _byteLen <= _end) {
					_result += TargetStr.substring( i, i+1 );
				}
			}
		}
		return _result.trim();
	}

	/**
	 * 문자열을 받아서 null이면 공백 문자열로 리턴
	 * @author 김한섭
	 * @param str 문자열
	 * @return str
	 */
	public static String ifNull(String str)
	{
		if ((str == null) || (str.trim().equals("")) || (str.trim().equals("null")))
			return "";
		else
			return str;
	}
	/**
	 * 문자열을 받아서 숫자만 리턴 한다
	 * @author 정혁채
	 * @param str 문자열
	 * @return str
	 */
	public static int[] getIntArray(String[] str)
	{
		int[] tempInt = new  int[str.length];
		int[] rn;
		int poi = 0;
		for (int i=0;i<str.length;i++){
			try{
				tempInt[poi] = Integer.parseInt(str[i]);
				poi++;
			}catch(Exception e){	}
		}
		rn = new int[poi];
		for (int i=0;i<poi;i++){
			rn[i] = tempInt[i];
		}
		return rn;
	}

	/**
	 * 문자열을 받아서 숫자만 리턴 한다
	 * @author 정혁채
	 * @param str 문자열
	 * @return str
	 */
	public static String getOnlyNumber(String str)
	{
		String rn = "";
		for (int i=0;i<str.length();i++){
			try{
				if (!str.substring(i,i+1).equals("\\-")){
					Integer.parseInt(str.substring(i,i+1));
				}
				rn +=str.substring(i,i+1);
			}catch(Exception e){	}
		}
		return rn;
	}
	/**
	 * 문자열을 받아서 숫자만 리턴 한다
	 * @author 정혁채
	 * @param str 문자열
	 * @return str
	 */
	public static String getOnlyFloat(String str)
	{
		String rn = "";
		for (int i=0;i<str.length();i++){
			try{
				if (!str.substring(i,i+1).equals(".")&& !str.substring(i,i+1).equals("-"))	Integer.parseInt(str.substring(i,i+1));
				rn +=str.substring(i,i+1);
			}catch(Exception e){	}
		}
		return rn;
	}
	/**
	 * 입력 문자열을 입력 길이만큼 parttern문자를 왼쪽에 채워 리턴
	 *
	 * @param str 입력 문자열
	 * @param parttern 채울 문자
	 * @param length 총 길이
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        String date = StringUtil.getLPad("123", "0", 5);
     *  결과 : 00123
     * </pre>
	 *
	 */
	public static String getLPad(String str, String parttern, int length) {

		String strTmp = "";
		int nStr = str.length();

		if ( parttern.length() != 1 ) { return ""; }
		if ( nStr == length ) { return str; }
		if ( nStr > length ) { return ""; }

		for ( int inx = 0 ; inx < (length - nStr) ; inx++ ) {
			strTmp += parttern;
		}

		return strTmp + str;
	}

	/**
	 * 입력 문자열을 입력 길이만큼 parttern문자를 오른쪽에 채워 리턴
	 *
	 * @param str 입력 문자열
	 * @param parttern 채울 문자
	 * @param length 총 길이
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        String date = StringUtil.getRPad("123", "0", 5);
     *  결과 : 12300
     * </pre>
	 */
	public static String getRPad(String str, String parttern, int length) {

		String strTmp = "";
		int nStr = str.length();

		if ( parttern.length() != 1 ) { return ""; }
		if ( nStr == length ) { return str; }
		if ( nStr > length ) { return ""; }

		for ( int inx = 0 ; inx < (length - nStr) ; inx++ ) {
			strTmp += parttern;
		}

		return str + strTmp;
	}
	/**
	 * 입력 문자열이 NULL인지 체크
	 *
	 * @param str NULL 체크 문자열
	 * @return boolean
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.isNull(null);
     *  결과 : true
     * </pre>
	 */
	public static boolean isNull(String str) {

		boolean nRet = false;

		if ( str == null ) { nRet = true; }

		return nRet;
	}

	/**
	 * 입력 문자열이 NULL인지 체크하여 문자열 대체
	 *
	 * @param str NULL 체크 문자열
	 * @param changeStr 대체 문자열
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        String date = StringUtil.ifNull(null, "Empty");
     *  결과 : Empty
     * </pre>
	 */
	/* 로직에러
	public static String ifNull(String str, String changeStr) {

		String retStr = "";
		if ( isNull(str) && !isNull(changeStr) ) {
			retStr = changeStr;
		} else {
			retStr = null;
		}

		return retStr;
	}
	*/
	/**
	 * 입력 문자열이 비어있는지 체크
	 *
	 * @param str 입력 문자열
	 * @return boolean
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.isEmpty("");
     *  결과 : true
     * </pre>
	 */
	public static boolean isEmpty(String str) {
		boolean nRet = false;
		if (str == null || str.equals("") ) { nRet = true; }
		return nRet;
	}

	/**
	 * 입력 문자열을 trim하여 비어있는지 체크
	 *
	 * @param str 입력 문자열
	 * @return boolean
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.isEmptyTrim("    ");
     *  결과 : true
     * </pre>
	 */
	public static boolean isEmptyTrim(String str) {
		return isEmpty(str.trim());
	}

	/**
	 * 문자열을 왼쪽에서 부터 짤라온다
	 *
	 * @param str 문자열
	 * @param index 인덱스
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.getLeft("ABCDEFGHI", 3);
     *  결과 : ABC
     * </pre>
	 */
	public static String getLeft(String str, int index) {
		return str.substring(0, index);
	}

	/**
	 * 문자열을 오른쪽에서 부터 짤라온다
	 *
	 * @param str 문자열
	 * @param index 인덱스
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.getRight("ABCDEFGHI", 3);
     *  결과 : GHI
     * </pre>
	 */
	public static String getRight(String str, int index) {
		return str.substring(str.length() - index, str.length());
	}

	/**
	 * 문자열 가운데를 짤라온다
	 *
	 * @param str 문자열
	 * @param startindex 시작인덱스
	 * @param chrcount 문자개수
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.getMid("ABCDEFGHI", 4, 3);
     *  결과 : DEF
     * </pre>
	 */
	public static String getMid(String str, int startindex, int chrcount) {
		return str.substring(startindex - 1, startindex - 1 + chrcount);
	}

	/**
	 * 문자열의 길이가 length와 같은지 체크
	 *
	 * @param str 입력문자열
	 * @param length 문자열길이
	 * @return boolean
	 *
     * <p><pre>
     *  - 사용 예
     *        boolean date = StringUtil.isLengh("ABCDEFGHI", 9);
     *  결과 : true
     * </pre>
	 */
	public static boolean isLengh(String str, int length) {
		boolean nRet = false;
		if ( str.length() == length ) {
			nRet = true;
		}
		return nRet;
	}

	/**
	 * 입력 문자열이 NULL인지 체크하여 문자열 대체
	 *
	 * @param str NULL 체크 문자열
	 * @param changeStr 대체 문자열
	 * @return String
	 *
     * <p><pre>
     *  - 사용 예
     *        String date = StringUtil.ifNull(null, "Empty");
     *  결과 : Empty
     * </pre>
	 */
	public static Date convNull(Object obj, Date def) {
       if (obj == null)
           return def;
       else
           return (Date)obj;
   }
	public static String convNull(String str, String def) {
		if (isNull(str) || isEmpty(str))
			return def;
		return str;
	}
	public static String convNull(Object obj, String def) {
		if (obj == null)
			return def;
		else
			return String.valueOf(obj);
	}
	public static int convNull(Object obj, int def) {
	   if (obj == null || obj.equals(""))
	      return def;
	   else
	      return Integer.parseInt(obj.toString());
	}
	
	public static double convNull(Object obj, double def) {
	   if (obj == null || obj.equals(""))
	      return def;
	   else
	      return Double.parseDouble(obj.toString());
	}
	
	public static Long convNull(Object obj, Long def) {
	       if (obj == null || obj.equals(""))
	          return def;
	       else
	          return Long.parseLong(obj.toString());
	    }

	/**
	 * 쿠키를 가져온다
	 * @param request HttpServletRequest
	 * @param key 쿠키 키
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String key){
		Cookie[] cookies = request.getCookies();
		String rtnValue = "";
		if(cookies != null){
			for(int i=0; i<cookies.length; i++){
				if(cookies[i].getName().equals(key)){
					rtnValue = cookies[i].getValue();
					break;
				}
			}
		}

		return rtnValue;
	}

	/**
	 * 쿠키 값 설정
	 * @param response
	 * @param key 쿠키 키
	 * @param value 쿠키 값
	 */
	public static void setCookie(HttpServletResponse response, String key, String value){
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		response.addCookie(cookie);
	}


	/**
	 * 쿠키 저장
	 * @param response
	 * @param key 쿠키의 키
	 * @param value 쿠키의 값
	 * @param interval 쿠키의 저장시간(초)
	 * @author Jeong, jaeheon
	 */
	public static void setCookie(HttpServletResponse response, String key, String value, int interval){
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(interval); //10년간 쿠키 저장
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/**
	 * 날짜 구분자에 대해 네거티브 처리되어 리턴(구분자-)
	 * @param response
	 * @param date 날짜 문자열 데이터
	 */
	public static String getDateFormat(String date){
		return getDateFormat(date, "-");
	}


	/**
	 * 날짜 구분자에 대해 네거티브 처리되어 리턴 //조건추가 7이하길이일때는 그대로 리턴
	 * @param response
	 * @param date 날짜 문자열 데이터
	 * @param sp 구분자
	 */
	public static String getDateFormat(String date, String sp){
		String rtn = "";
		if(date != null &&!date.equals("")){
			if(date.indexOf("-") > -1){
				rtn = date.replace(String.valueOf(sp), "");
			}else{
				if(date.length()>7){
					rtn = date.substring(0, 4) + sp + date.substring(4, 6) + sp + date.substring(6, 8);
				}else if(date.length()==6){
                    rtn = date.substring(0, 4) + sp + date.substring(4, 6);
                }else{
					rtn = date;
				}
			}
		}

		return rtn;
	}
	
	/**
	 * 
	 * @param response
	 * @param time 날짜 문자열 데이터 ( 123456 -> 12:34:56 / 1234 -> 12:34 )
	 * 
	 */
	public static String getTimeFormat(String time){
	   String rtn = "";
	   String sp = ":";
	   if(time != null &&!time.equals("")){
	      if(time.indexOf("-") > -1){
	         rtn = time.replace(String.valueOf(sp), "");
	      }else{
	         if(time.length()>5){
	            rtn = time.substring(0, 2) + sp + time.substring(2, 4) + sp + time.substring(4, 6);
	         }else{
	            rtn = time.substring(0, 2) + sp + time.substring(2, 4);
	         }
	      }
	   }
	   
	   return rtn;
	}

	/**
	 * 날짜 구분자에 구분자(-)포함
	 * @param response
	 * @param date 날짜 문자열 데이터
	 */
	public static String getDateFormatSet(String date){
		String rtn = "";
		if(date != null &&!date.equals("")){
			if(date.indexOf("-") == -1){
				if(date.length()>7){
					rtn = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
				}else{
					rtn = date;
				}
			}
		}

		return rtn;
	}
	/**
	 * 날짜 유형 변경
	 * @param response
	 * @param date 날짜 문자열 데이터
	 */
	public static String getDateCustFormatSet(String date, String sp){
		Date  xls_transDt = null;
		String tempTransDt = null;
		SimpleDateFormat dateFormat[] = {new SimpleDateFormat("yyyyMMdd")
		,new SimpleDateFormat("yyyy-MM-dd")
		,new SimpleDateFormat("yyyy/MM/dd")
		,new SimpleDateFormat("yyyy.MM.dd")
		,new SimpleDateFormat("yyMMdd")
		,new SimpleDateFormat("yy-MM-dd")
		,new SimpleDateFormat("yy/MM/dd")
		,new SimpleDateFormat("yy.MM.dd")};

		for(int x=1; x< dateFormat.length ; x++){
			try{
				xls_transDt = dateFormat[x].parse(date);
				break;
			}catch(Exception pe){	}
		}
		if(xls_transDt != null){
			tempTransDt = dateFormat[0].format(xls_transDt);
			if (tempTransDt.substring(0,1).equals("0")) tempTransDt = "2"+tempTransDt.substring(1,tempTransDt.length());
			tempTransDt = tempTransDt.replace("-","").replace("/","").replace(".","").replace(" ","");
		}else{
			return "";
		}
		return (!sp.equals("")) ? getDateFormat(tempTransDt, sp) : tempTransDt;
	}

	/**
	 * 날짜 구분자에 대해 네거티브 처리되어 리턴(구분자-)
	 * @param response
	 * @param date 날짜 문자열 데이터
	 */
	public static String getDateRemoveFormat(String date, String sp){
		if(date!=null)
			return date.replace(sp, "");

		return date;
	}

	/**
	 * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
	 * Date type 일 경우
	 * @param date
	 * @param dateType
	 * @return
	 * @throws Exception
	 */
	public static String getDateDay(Date date) throws Exception {
	 
	    String day = "" ;
	   // SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
	    // Date nDate = dateFormat.parse(date) ;
	   // String nDateTxt = dateFormat.format(date);
	    
	    Calendar cal = Calendar.getInstance() ;
	    cal.setTime(date);
	     
	    int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
	     
	    switch(dayNum){
	        case 1:
	            day = "일";
	            break ;
	        case 2:
	            day = "월";
	            break ;
	        case 3:
	            day = "화";
	            break ;
	        case 4:
	            day = "수";
	            break ;
	        case 5:
	            day = "목";
	            break ;
	        case 6:
	            day = "금";
	            break ;
	        case 7:
	            day = "토";
	            break ;
	    }
	     
	    return day ;
	}
	
	/**
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     * String type 일 경우
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String getStringDateDay(String date, String dateType) throws Exception {
     
        String day = "" ;
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;
       // String nDateTxt = dateFormat.format(date);
        
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);
         
        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
         
        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;
        }
         
        return day ;
    }
	/*
	 date형식을 String으로 변환
	 */
	public static String dateToString(Date date, String sp)  throws Exception{
	   String dateTxt = "";
	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
	   dateTxt = simpleDateFormat.format(date);
	   dateTxt =  getDateCustFormatSet(dateTxt, sp);
	   return dateTxt;
	}
	
	/*  시간 형식 변경
	 *  10:00:00 -> 10:00
	 */
	public static String convertTime(String time)  throws Exception{
	   String timeTxt = StringUtil.convNull(time, "");
	   if(!timeTxt.equals("")){
	   
	      timeTxt = timeTxt.replaceAll(":", "");
	      timeTxt = timeTxt.length() == 3 ? "0" + timeTxt : timeTxt;
               
       if(timeTxt.length() == 4){
         return timeTxt.substring(0,2) + ":" + timeTxt.substring(2,4);
       }
     }
     return "";
	}
	
	/*
	 * Date를 년월일시분으로 리턴
	 * date 또는 dateStr 둘중에 하나만 전달
	 * dateStr 2020.05.19 ( 또는 2020-05-19 ) + 14:00:30 ( 시간은 없어도 됨 (초 단위 없어도 됨 ) )
	 * 날짜와 시간 사이에 반드시 공백이 있어야함
	 */
	public static String convertDateKr ( Date date, String dateStr ) {
	   String result = "";

	   if( dateStr == null){
		   SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyyMMddHHmmss");
		   String dateIsString = dateFormat.format(date);

		   result += dateIsString.substring(0, 4) + "년 ";
		   result += dateIsString.substring(4, 6) + "월";
		   result += dateIsString.substring(6, 8) + "일 ";
		   result += dateIsString.substring(8, 10) + "시";
		   result += dateIsString.substring(10, 12) + "분";
		   // result += dateStr.substring(12, 14) + "초 ";
	   }
	   else {
		   String[] dateGubun = dateStr.split(" ");

		   String dateIsString = dateGubun[0].replaceAll("-","");

		   dateIsString = dateIsString.substring(0,4) + "년 " + dateIsString.substring(4,6) + "월 " + dateIsString.substring(6,8) + "일";

		   if(dateGubun.length == 2 ){
		   		String[] timeGubun = dateGubun[1].split(":");
			    String timeIsString = timeGubun[0] + "시 " + timeGubun[1] + "분";
			    if(timeGubun.length == 3) timeIsString += " " + timeGubun[2] + "초";
			   dateIsString += (" " + timeIsString);
		   }

		   result = dateIsString;
	   }

	   
	   return result;
	}
	
	/*
	 * date 에서 ms( 밀리세컨드 ) 만큼의 시간이 지난 후의 날짜를 리턴 
	 */
	public static Date datePassedMs( int ms, Date date, String dateFormat ) throws Exception {

	   SimpleDateFormat simpleDate = new SimpleDateFormat(dateFormat);
       int day = ms / (60 * 60 * 24);
       
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       cal.add(Calendar.DATE , day);
       
       Date passDate = new Date(cal.getTimeInMillis());
       passDate = simpleDate.parse(simpleDate.format(passDate));
	   
	   return passDate;
	}
	/**
	 * 숫자에 콤마추가하여 리턴
	 * @param response
	 * @param date 금액 문자열 데이터
	 */
	public static String getNumberSetComma(String number){
		String rtn = "";
		if(number != null && !number.equals("")){
			try{
				if(number.length()>3){
					if(number.indexOf(".")==-1){
						rtn = new DecimalFormat("#,###").format(Long.parseLong(number));
					}else{
						String s1 = number.split("\\.")[0];
						rtn = new DecimalFormat("#,###.#").format(Long.parseLong(s1)) + "." + number.split("\\.")[1];
					}
					/*
					if(number.indexOf(".")==-1)
						rtn = new DecimalFormat("#,###").format(Long.parseLong(number));
					else
						rtn = new DecimalFormat("#,###.#").format(Float.parseFloat(number));
					*/
				}
				else
					rtn = number;
			}catch(IllegalArgumentException e){
				rtn = "0";
			}
		}

		return rtn;
	}

	/**
	 * 숫자에 콤마 삭제후 리턴
	 * @param response
	 * @param date 금액 문자열 데이터
	 */
	public static String getNumberRemoveComma(String number){
		String rtn = "0";
		if(number != null && !number.equals("")){
			try{
				rtn = number.replace(",", "");
			}catch(IllegalArgumentException e){
				rtn = "0";
			}
		}

		return rtn;
	}


	/**
	 * 숫자 콤마에 대해 네거티브 처리되어 리턴
	 * @param response
	 * @param date 금액 문자열 데이터
	 */
	public static String getNumberFormat(String number){
		String rtn = "";
		if(number != null && !number.equals("")){
			try{
				if(number.indexOf(",") > -1){
					rtn = getNumberRemoveComma(number);
				}else{
					rtn = getNumberSetComma(number);
				}
			}catch(IllegalArgumentException e){
				rtn = "0";
			}
		} else {
		  rtn = "0";
		}

		return rtn;
	}


	/**
	 * 숫자 콤마에 대해 네거티브 처리되어 리턴
	 * @param response
	 * @param date 금액 문자열 데이터
	 */
	public static String getNumberFormat(Object number){
		return getNumberFormat(String.valueOf(number));
	}


	/**
	 * 지정된 갯수만큼 생성하여 ArrayList 타입으로 리턴
	 * @param start - 시작 수치
	 * @param end - 종료 수치
	 */
	public static ArrayList<String> getLoopNumber(int start, int end){
		ArrayList<String> sNumber = new ArrayList<String>();

		if(start > end) {
			for(int i = start; i >= end; i--){
				sNumber.add(String.valueOf(i));
			}
		}else{
			for(int i = start; i <= end; i++){
				sNumber.add(String.valueOf(i));
			}
		}
		return sNumber;
	}


	/**
	 * 년도 ArrayList 타입으로 리턴
	 * @param startYear
	 * @param endYear
	 */
	public static ArrayList<String> getYearList(int startYear, int endYear){
		ArrayList<String> sYear = new ArrayList<String>();

		sYear = getLoopNumber(startYear, endYear);

		return sYear;
	}


	/**
	 * 년도 ArrayList 타입으로 리턴
	 * @param 현날짜 기준으로 + 수치
	 * @param 현날짜 기준으로 - 수치
	 * @param 정렬방식 - D:내림 A:오름차순
	 */
	public static ArrayList<String> getYearList(int next, int prev, String order){
		ArrayList<String> sYear = new ArrayList<String>();

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int startYear = year + next;
		int endYear = year - prev + 1;

		int temp = startYear;
		if(order.equals("A")){
			startYear = endYear;
			endYear = temp;
		}

		sYear = getLoopNumber(startYear, endYear);

		return sYear;
	}


	/**
	 * 문자열에서 특정 문자 공백처리
	 * @param 변경할 문자열
	 * @param 공백처리할 문자열
	 */
	public static String replaceString(String str, String repChar){
		str = convNull(str, "");
		return str.replace(repChar, "");
	}


	/**
	 * json 보내기 전 불필요한 key제거
	 * @param 원본 객체
	 * @param 제거할 key
	 */
	public static ArrayList<Map<String, Object>> filterMapResult(ArrayList<Map<String, Object>> src, String[] splitKey){
		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(int i=0; i<src.size(); i++){
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.putAll(src.get(i));
			for(int j=0; j<splitKey.length; j++){
				mapData.remove(splitKey[j]);
			}

			mapList.add(mapData);
		}

		return mapList;
	}

	public static String makeMoneyType(double dblMoneyString) {
        String moneyString = new Double(dblMoneyString).toString();

        String format = "#,##0";
        DecimalFormat df = new DecimalFormat(format);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();

        dfs.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setDecimalFormatSymbols(dfs);

        return (df.format(Double.parseDouble(moneyString))).toString();
	}
	
	public static String makeMoneyType(String dblMoneyString) {
	   dblMoneyString = dblMoneyString.replaceAll(",", "");
	   String format = "#,##0";
	   DecimalFormat df = new DecimalFormat(format);
	   DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	   
	   dfs.setGroupingSeparator(',');
	   df.setGroupingSize(3);
	   df.setDecimalFormatSymbols(dfs);
	   String money = (df.format(Double.parseDouble(dblMoneyString))).toString();
	   if(money.length() != 0){
	      money = money.substring(0, money.length()-1) + "0";
	   }else{
	      money = "0";
	   }
	   return money;
	}


	/**
	 * 카드번호, 계좌번호 ** 처리
	 * @param 번호문자열
	 */
	public static String replaceNumberHidden(String number){
		String tmp = "";

		if(number!=null && !number.equals("")){
			String[] tmpAr = number.split("-");

			if(tmpAr.length>2){
				for(int i=0; i<tmpAr.length; i++){
					if(!tmp.equals("")) tmp += "-";

					if(i==tmpAr.length-2)
						tmp += StringUtil.getLPad("", "*", tmpAr[i].length());
					else
						tmp += tmpAr[i];
				}
			}else{ // 암호화 적용안함
				if(number.length()>6)
					tmp = number.substring(0, number.length()-4)+"****"; //카드번호 암호화 적용
				else
					tmp = number;
			}
		}

		return tmp;
	}

	/**
	 * 요청사이트가 현재 접속중인 사이트 인지 확인
	 * @param HttpServletRequest
	 */
	public static boolean checkDomain(HttpServletRequest req) throws Exception{
		String refer = convNull(req.getHeader("referer"), "");
		String url = req.getRequestURL().toString();

		if(refer.equals("") ){
			refer = refer.split("//")[1].split("/")[0];
		}
		url = url.split("//")[1].split("/")[0];

		if(refer.equals(url) || refer.equals(""))
			return false;
		else
			return true;
	}

	/*
	 * 개인정보호법에 의한 정보 처리
	 * @param 처리항목 : CARD, BANK, PHONE, NAME, ID, JUMIN, IP
	 * @param 처리값
	 */
	public static String changePrivateInfoData(String kind, String data){
		String rtnData = data;
		
		if(kind.equals("CARD") || kind.equals("BANK") || kind.equals("PHONE") ){
			rtnData = replaceNumberHidden(rtnData);
		}else if(kind.equals("NAME")){
			if(rtnData!=null && !rtnData.equals("")){
				if(rtnData.length()>1)
					rtnData = rtnData.substring(0, rtnData.length()-1) + "*";
			}
		}else if(kind.equals("ID")){
			if(rtnData!=null && !rtnData.equals("")){
					String[] rtnDataArray=rtnData.split("@");
				if(rtnDataArray[0].length()>4){
					rtnData = rtnDataArray[0].substring(0, 4) + "****";
					if(rtnDataArray.length>1) rtnData += "@" + rtnDataArray[1];
				}else {
					rtnData = rtnDataArray[0].substring(0, 2) + "****";
					if(rtnDataArray.length>1) rtnData += "@" + rtnDataArray[1];
				}
			}
		}else if(kind.equals("BIZNO")){
			if(rtnData.length()<13){
				rtnData = replaceNumberHidden(rtnData);
			}else{
				rtnData = rtnData.split("-")[0]+"-*******";
			}
		}else if(kind.equals("JUMIN")){
			if(rtnData.length()==13){
				String[] temp = rtnData.split("-");
				if(temp.length == 2) {
					rtnData = rtnData.split("-")[0]+"-"+ rtnData.split("-")[1].substring(0,1) +"******";
				}else {
					rtnData = rtnData.split("-")[0];
				}
			}
		}else if(kind.equals("HEAD_NAME")){
			if(rtnData!=null && !rtnData.equals("")){
				rtnData = "*"+rtnData.substring(1, rtnData.length());
			}
		}else if(kind.equals("IP")){
			if(rtnData!=null && !rtnData.equals("")){
				String[] ar = rtnData.split("\\.");
				rtnData = "";
				for(int i=0; i<ar.length; i++){
					if(!rtnData.equals("")){
						rtnData += ".";
					}
					if(ar.length>2 && i==ar.length-2){
						for(int j=0; j<ar[i].length(); j++){
							rtnData += "*";
						}
					}else{
						rtnData += ar[i];
					}
				}
			}
		}

		return rtnData;
	}

	/*
	 * 개인정보호법에 의한 정보 처리
	 * @param 처리항목 : CARD, BANK, PHONE, NAME
	 * @param 처리값 ArrayList<Map<String, Object>>
	 * @param 변경할 키
	 */
	public static ArrayList<Map<String, Object>> changePrivateInfoData(String kind, ArrayList<Map<String, Object>> data, String key){
		ArrayList<Map<String, Object>> rtnData = data;

		if(kind.equals("CARD") || kind.equals("BANK") || kind.equals("PHONE")){
			for(int i=0; i<rtnData.size(); i++){
				rtnData.get(i).put(key, replaceNumberHidden((String)rtnData.get(i).get(key)));
			}
		}else if(kind.equals("NAME")){
			if(rtnData!=null){
				for(int i=0; i<rtnData.size(); i++){
					String temp = (String)rtnData.get(i).get(key);
					rtnData.get(i).put(key, temp.substring(0, temp.length()-1) + "*");
				}
			}
		}else if(kind.equals("ID")){
			if(rtnData!=null){
				for(int i=0; i<rtnData.size(); i++){
					String temp = (String)rtnData.get(i).get(key);
					rtnData.get(i).put(key, temp.substring(0, temp.length()-2) + "**");
				}
			}
		}

		return rtnData;
	}

	/**
	 * 올더게이트 데이터 hash처리
	 * @param 상점아이디
	 * @param 주문번호
	 * @param 결제금액
	 */
	public static String createAgsHashData(String storeId, String ordNo, int amt) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(storeId);
		sb.append(ordNo);
		sb.append(amt);

		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();
		for (int i=0 ; i < digest.length ; i++) {
		 int c = digest[i] & 0xff;
		 if (c <= 15){
			 strBuf.append("0");
		 }
		 strBuf.append(Integer.toHexString(c));
		}

		return strBuf.toString();
	}

	/**
	 * SSL 도메인 얻기
	 * @param req
	 * @bSSL SSL처리된 도메인리턴 여부(boolean) - false의 경우 도메인리턴
	 * @return 처리된 도메인명(프로토콜포함)
	 * @throws IOException
	 */
	public static String getSSLDomain(HttpServletRequest req, boolean bSSL) throws IOException{

		Properties properties = PropertiesLoaderUtils.loadAllProperties("common.properties");
		String SSLYn = properties.getProperty("common.SSLYn");

		String sslDomain = "";
		if(bSSL == true){
			if(SSLYn.trim().equals("Y")){
				String port = String.valueOf(req.getServerPort());
				if(port=="80" || SSLYn.trim().equals("Y")) port = "";
				else port = ":" + port;
				//sslDomain = "https://www.xn--ob0bs79awa206c7ov.com";
				sslDomain = "https://" + req.getServerName() + port;
			}
		}else{
			String port = String.valueOf(req.getServerPort());
			if(port=="80" || SSLYn.trim().equals("Y")) port = "";
			else port = ":" + port;

			sslDomain = "http://" + req.getServerName() + port;
		}

		return sslDomain;
	}

	/**
	 * SSL 도메인 얻기
	 * @param req
	 * @bSSL SSL처리된 도메인리턴 여부(boolean) - false의 경우 도메인리턴
	 * @return 처리된 도메인명(프로토콜포함)
	 * @throws IOException
	 */
	public static String getSSLDomain(HttpServletRequest req) throws IOException{
		return getSSLDomain(req, true);
	}

	public static String getLeft2(long timeInMillis, int index) {
		// TODO Auto-generated method stub
		String timeMinute = Long.toString(timeInMillis);
		System.out.println("getLeft2 : " + timeMinute);
		return timeMinute.substring(0, index);
	}

	/**
	 * 문자열 배열 역순으로 재배치(String)
	 * @param data
	 * @return
	 */
	public static String[] reverseArray(String data){
		if(data !=null){
			String[] temp = new String[data.length()];

			for(int i=data.length()-1, idx=0; i>=0; i--, idx++){
				temp[idx] = String.valueOf(data.charAt(i));
			}
			return temp;
		}else{
			return null;
		}
	}

	/**
	 * 문자열 배열 역순으로 재배치(String[])
	 * @param data
	 * @return
	 */
	public static String[] reverseArray(String[] data){
		if(data !=null){
			String[] temp = data.clone();

			if(data != null){
				for(int i=data.length-1, idx=0; i>=0; i--, idx++){
					temp[idx] = data[i];
				}
			}
			return temp;
		}else{
			return null;
		}
	}

	/**
	 * 배열 역순으로 재배치(ArrayList)
	 * @param data
	 * @return
	 */
	public static ArrayList reverseArray(ArrayList data){
		if(data != null)
			Collections.reverse(data);

		return data;
	}

	/**
	 * Long형으로 형변환 null은 0으로
	 * @param data
	 * @return
	 */
	public static Long toLong(Object data){
		String temp = "0";
		if(data != null && !data.equals(""))
			temp = String.valueOf(data);

		return Long.valueOf(temp);
	}

	/**
	 * Int형으로 형변환 null은 0으로
	 * @param data
	 * @return
	 */
	public static int toInt(Object data){
		String temp = "0";
		if(data != null && !data.equals(""))
			temp = String.valueOf(data);

		return Integer.valueOf(temp);
	}

	/**
	 * 배열 길이만큼 초기화
	 * @param list
	 * @param count
	 * @param data
	 * @return
	 */
	public static ArrayList<Object> initArrayList(int count, Object data){
		ArrayList<Object> list = new ArrayList<Object>();
		for(int i=0; i<count; i++){
			list.add(data);
		}
		return list;
	}

	/**
	 * 배열 길이만큼 초기화
	 * @param list
	 * @param count
	 * @param data
	 * @return
	 */
	public static ArrayList<Object> initArrayList(int count){
		return initArrayList(count, null);
	}

	/**
	 * 해당월의 마지막 일자
	 * @param date : yyyymm
	 * @return String
	 */
	public static String getLastDay(String date){
		String lastDay=null;
		if(date != null && !date.equals("")){
			Calendar cal = Calendar.getInstance();
			int year = Integer.valueOf(date.substring(0,4));
			int month = Integer.valueOf(date.substring(5,7));

			cal.set(year, month-1, 1);
			lastDay = String.valueOf(cal.getActualMaximum(Calendar.DATE));
		}

		return lastDay;
	}

	/**
	 * 해당월의 마지막 일자
	 * @param date : Date타입
	 * @return String
	 */
	public static String getLastDay(Date date){
		String lastDay=null;
		if(date != null && !date.equals("")){
			Calendar cal = Calendar.getInstance();
			cal.set(date.getYear(), date.getMonth()+1, 1);
			lastDay = String.valueOf(cal.getActualMaximum(Calendar.DATE));
		}

		return lastDay;
	}

	/**
    *  복사 기능을 수행
    *      - 타겟 디렉토토리가 없는 경우 만듬
    *      - 소스가 디렉토리인지 아닌지를 구분하여 실행
    *        디렉토리 --> 디렉토리안의모든 파일을 목적 디렉토리로 복사
    *        파일    --> 해당 파일을 타겟 디렉토리로 이동
    * @param sourceLocation : 소스 경로
    * @param targetLocation : 목적지 경로
    * @throws IOException
    */
    public static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {

            //소스가 디렉토리인 경우
            // --> 해당 디렉토리의 모든 파일을 타겟 디렉토리에 복사
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        }
        // 소스가 파일 인경우 --> 해당 파일을 타겟 디렉토리로 이동
        else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

	/**
    *  폴더 생성
    * @param file : 생성 경로
    * @throws IOException
    */
    public static void makeFolder(File file) {
		if (file.exists()) return;
    	file.mkdir();
	}

	/**
    *  폴더 삭제
    * @param file : 삭제 경로
    * @throws IOException
    */
    public static void deleteFolder(File file) {
		if (!file.exists()) return;
		File[] files = file.listFiles();
		if(files != null){
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolder(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		file.delete();
	}
	/**
	 * 사업자 번호 체크
	 * @param response
	 * @param date 금액 문자열 데이터
	 */
    public static boolean checkBizID(String bizID){ // bizID는 숫자만 10자리로 해서 문자열로 넘긴다.
		int checkID []={1, 3, 7, 1, 3,7, 1, 3, 5, 1};
		int chkSum=0;
		String c2;
		int remander;
		bizID = bizID.replaceAll("\\-","");
		if(bizID.length() <13){
			for (int i=0; i<=7; i++){
				chkSum = chkSum+((checkID[i]) * Integer.parseInt(String.valueOf(bizID.charAt(i))));
			};
			c2 = "0" + ((checkID[8]) * bizID.charAt(8));
			c2 = c2.substring(c2.length() - 2, c2.length());
			chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1));
			remander = (10 - (chkSum % 10)) % 10 ;

			if ( Integer.parseInt(String.valueOf(bizID.charAt(9))) == remander){
				return true ; // OK!
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
    
    /**
     * 주민등록번호 유효성 체크
     * @param jumin
     * @return
     */
    public static boolean checkJumin(String jumin){
      if(jumin == null) return true;

      jumin = jumin.replaceAll("-", "");

      //모든 문자가 수자로 이루어졌는지 체크한다.
      Pattern p = Pattern.compile("\\d{13}");
      if (!p.matcher(jumin).matches())
      return false;


      int total = 0;
      int[] ssns = transNumberStringToIntArray(jumin);
      int[] ns = { 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5 };

      for (int i = 0; i < ns.length; i++) {
        total += ssns[i] * ns[i];
      }

      if (ssns[ssns.length -1]  == ((11 - total % 11) % 10))
        return true;
      return false;
    }

    private static int[] transNumberStringToIntArray(String numString){
      int[] results = new int[numString.length()];
      for (int i = 0; i < results.length; i++) {
        results[i] = numString.charAt(i) - '0';
      }
      return results;
    }

    
    /**
     * 법인번호 유효성 체크
     * @param no
     * @return
     */
    public static boolean checkBubin(String no){
      if(no == null) return true;

      no = no.trim().replaceAll("-", "");
      if(no.equals("") || no.length() != 13) return false;

      int[] x = new int[14];
      int i = 0;
      int tmp = 0;
      int s = 0;
      int digit = 0;
      for(i = 0; i < 13; i++){
          x[i] = Integer.parseInt(no.substring(i,i+1));
      }

      for(i = 0; i < 12; i++){
          if(i%2 == 0) s = 1;
          else s = 2;

          tmp = tmp + (x[i] * s);
      }
      tmp = tmp%10;
      digit = 10 - tmp;
      if(digit == 10) digit = 0;
      if(digit == x[12]){
        return true;
      }else{
        return false;
      }
    }
    
	/**
	 * 사업자 번호 또는 주민/법인 번호 네거티브 처리
	 * @param response
	 * @param date 금액 문자열 데이터
	 */
	public static String getBizFormat(String str){
		String rtn = "";
		if(str != null && !str.equals("") && str.length()==13){ // 주민번호/법인
			String regex = "(\\d{6})(\\d{7})";
			rtn = str.replaceAll(regex, "$1-$2");
		}else if(str != null && !str.equals("") && str.length()==10){ // 사업자
			String regex = "(\\d{3})(\\d{2})(\\d{5})";
			rtn = str.replaceAll(regex, "$1-$2-$3");
		}else if(str != null && !str.equals("")){
			rtn = str.replace("-", "");
		}

		return rtn;
	}
	/**
	 * 사업자 번호 또는 주민/법인 번호 네거티브 처리
	 * @param response
	 * @param date 금액 문자열 데이터
	 * @param flag true:무조건 -포함, false:네거티브
	 */
	public static String getBizFormat(String str, boolean flag){
		if(flag == true && str != null && !str.equals("")){
			str = str.replace("-", "");
		}

		return getBizFormat(str);
	}

	/**
	 * 문자열을 전화번호 형식으로 변경
	 * @param str
	 * @return
	 */
	public static String getPhoneNoFormat(String str, boolean isRemove){
		String temp = str;
		if(str!=null && str!=""){
			if(isRemove == true){
				temp = str.replace("-", "");
			}else{
				String regex = "^(0(303|60|70|2|31|32|33|41|42|43|51|52|53|54|55|61|62|63|64|10|11|16|17|18|19))(\\d{3,4})(\\d{4})$";
				temp = str.replaceAll(regex, "$1-$3-$4");
			}
		}

		return temp;
	}

	/**
	 * 문자열을 각 은행별 계좌번호 형식으로 변경
	 * @param str
	 * @return
	 */
	public static String getBankNoFormat(String comCd, String no){
		if(comCd.length() == 3) comCd = comCd.substring(1);
		if(no.length() == 11){
			no = no.substring(0,3)+"-"+no.substring(3,5)+"-"+no.substring(5);
		}else if(no.length() == 12){
			if(comCd.equals("88")){
				no = no.substring(0,3)+"-"+no.substring(3,6)+"-"+no.substring(6);
			}else if(comCd.equals("31")){
				no = no.substring(0,3)+"-"+no.substring(3,5)+"-"+no.substring(5,11)+"-"+no.substring(11);
			}else if(comCd.equals("05") || comCd.equals("27")){
				no = no.substring(0,3)+"-"+no.substring(3,9)+"-"+no.substring(9);
			}else if(comCd.equals("04")){
				no = no.substring(0,3)+"-"+no.substring(3,5)+"-"+no.substring(5,9)+"-"+no.substring(9);
			}
		}else if(no.length() == 13){
			if(comCd.equals("20")){
				no = no.substring(0,4)+"-"+no.substring(4,7)+"-"+no.substring(7);
			}else if(comCd.equals("32") || comCd.equals("11")){
				no = no.substring(0,3)+"-"+no.substring(3,7)+"-"+no.substring(7,11)+"-"+no.substring(11);
			}
		}else if(no.length() == 14){
			if(comCd.equals("04")|| comCd.equals("71")){
				no = no.substring(0,6)+"-"+no.substring(6,8)+"-"+no.substring(8);
			}else if(comCd.equals("03")){
				no = no.substring(0,3)+"-"+no.substring(3,9)+"-"+no.substring(9,11)+"-"+no.substring(11);
			}else if(comCd.equals("81")){
				no = no.substring(0,3)+"-"+no.substring(3,9)+"-"+no.substring(9);
			}
		}
		return no;
	}

	/**
	 * parameterMap으로 받은 Map<String, String[]> = > Map<String,Object> 변환
	 * @param inputData
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> parameterMapSetting(Map<String, String[]> inputData) throws Exception{
		Map<String,Object> returnMap = new HashMap<String, Object>();

		Set<Entry<String, String[]>> entrySet = inputData.entrySet();
		Iterator<Entry<String, String[]>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String[]> entry = (Entry<String, String[]>) iterator.next();
			returnMap.put(entry.getKey(), entry.getValue()[0]);
		}
		return returnMap;
	}

	public static void outputHTML(HttpServletResponse rep, String msg) throws IOException{
		rep.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = rep.getWriter();
		writer.print(msg);
		writer.flush();
	}

	/**
	 * 모바일 여부 체크
	 * @param agent
	 * @return
	 */
	public static boolean isMobile(String agent){
		if(agent == null) return false;
		
		agent = agent.toLowerCase();
		if (agent.matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||agent.substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-")){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * 파일 이름 생성
	 * @param prefix
	 * @return String
	 */
	public static String getFilename(String prefix, String filenm){
		String filename = filenm;
		if(isNull(filenm) == false){
			int pos = filenm.lastIndexOf(".");
			String ext = filenm.substring(pos,filenm.length());
			String plusFilenm = String.valueOf(System.currentTimeMillis());
			String rndno = String.valueOf(Math.random()*1000).split("\\.")[0];

			filename = prefix + plusFilenm + "_" + rndno + ext;
		}

		return filename;
	}

	/**
	 * 결제정보 해쉬 암호화 적용( StoreId + OrdNo + Amt)
	 * MD5 해쉬데이터 암호화 검증을 위해
	 * @param storeId 상점아이디		form.StoreId.value
	 * @param ordNo 주문번호			form.OrdNo.value
	 * @param amt 금액					form.Amt.value
	 * @return HashData
	 * @throws NoSuchAlgorithmException
	 */
	public static String getAGSHashdata(String storeId, String ordNo, String amt) throws NoSuchAlgorithmException{
		 StringBuffer sb = new StringBuffer();
		 sb.append(storeId);
		 sb.append(ordNo);
		 sb.append(amt);

		 byte[] bNoti = sb.toString().getBytes();
		 MessageDigest md = MessageDigest.getInstance("MD5");
		 byte[] digest = md.digest(bNoti);

		 StringBuffer strBuf = new StringBuffer();
		 for (int i=0 ; i < digest.length ; i++) {
			 int c = digest[i] & 0xff;
			 if (c <= 15){
				 strBuf.append("0");
			 }
			 strBuf.append(Integer.toHexString(c));
		 }

		 return strBuf.toString();
	}

	public static String getCenterCd(String cd){
		String center_cd = "가상은행";

		if(cd.equals("39")) center_cd = "경남은행";
		if(cd.equals("34")) center_cd = "광주은행";
		if(cd.equals("04")) center_cd = "국민은행";
		if(cd.equals("11")) center_cd = "농협중앙회";
		if(cd.equals("31")) center_cd = "대구은행";
		if(cd.equals("32")) center_cd = "부산은행";
		if(cd.equals("02")) center_cd = "산업은행";
		if(cd.equals("45")) center_cd = "새마을금고";
		if(cd.equals("07")) center_cd = "수협중앙회";
		if(cd.equals("48")) center_cd = "신용협동조합";
		if(cd.equals("26")) center_cd = "(구)신한은행";
		if(cd.equals("05")) center_cd = "외환은행";
		if(cd.equals("20")) center_cd = "우리은행";
		if(cd.equals("71")) center_cd = "우체국";
		if(cd.equals("37")) center_cd = "전북은행";
		if(cd.equals("23")) center_cd = "제일은행";
		if(cd.equals("35")) center_cd = "제주은행";
		if(cd.equals("21")) center_cd = "(구)조흥은행";
		if(cd.equals("03")) center_cd = "중소기업은행";
		if(cd.equals("81")) center_cd = "하나은행";
		if(cd.equals("88")) center_cd = "신한은행";
		if(cd.equals("27")) center_cd = "한미은행";
		if(cd.equals("99")) center_cd = "가상은행";

		// 이지페이
		if(cd.equals("003")) center_cd = "가상은행";
		if(cd.equals("004")) center_cd = "국민은행";
		if(cd.equals("011")) center_cd = "농협중앙회";
		if(cd.equals("020")) center_cd = "우리은행";
		if(cd.equals("023")) center_cd = "SC제일은행";
		if(cd.equals("026")) center_cd = "신한은행";
		if(cd.equals("032")) center_cd = "부산은행";
		if(cd.equals("071")) center_cd = "우체국";
		if(cd.equals("081")) center_cd = "하나은행";

		return center_cd;
	}


	public static boolean isUTF8(byte[] sequence)
	{
		int numberBytesInChar;
		for (int i=0; i< sequence.length; i++){
			byte b = sequence[i];
			if (((b >> 6) & 0x03) == 2){
				return false;
			}
			byte test = b;
			numberBytesInChar = 0;
			while ((test & 0x80)>0){
				test <<= 1;
				numberBytesInChar ++;
			}

			if (numberBytesInChar > 1){       // check that extended bytes are also good...
				for (int j=1; j< numberBytesInChar; j++){
					if (i+j >= sequence.length){
						return false; // not a character encoding - probably random bytes
					}

					if (((sequence[i+j] >> 6) & 0x03) != 2){
						return false;
					}
				}
				i += numberBytesInChar - 1; // increment i to the next utf8 character start position.
			}
		}
		return true;
	}

	/**
	 * 문자열 결합
	 */
	public static String join(List<String> array, String sp) {
	    String result = "";

	    for (int i = 0; i < array.size(); i++) {
	      result += array.get(i);
	      if (i < array.size() - 1) result += sp;
	    }
	    return result;
	}

	/**
	 * 형변환
	 */
	public static String convertString(Object data) {
	  return String.valueOf(data);
	}

	/**
	 * 파일명 특수문자 제거
	 * < > | : * ? : " \ /
	 * @param name
	 * @return
	 */
	public static String StripFileName(String name) {
		String regex = "[<>|:*?:\"/\\\\ ]";
		if(name != null && !name.equals("")){
			name = name.replaceAll(regex, "");
		}else{
			name = "";
		}

		return name;
	}

	/**
	 * ArrayList타입의 데이터 암/복호화 // 리스트 * 처리 추가
	 * @param data 암/복호화 대상 ArrayList
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 ArrayList
	 */
	public static ArrayList<Map<String, Object>> removeDataField(ArrayList<Map<String, Object>> mapList, String[] key){
		for(int i=0; i<mapList.size(); i++){
			Map<String, Object> temp = mapList.get(i);
			for(int j=0; j<key.length; j++){
				temp.remove(key[j]);
			}
		}

		return mapList;
	}

	/*
	 * remote 아이피 확인
	 */
	public static String getRemoteAddr(HttpServletRequest req){
		String ip = req.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//System.out.println("1 >> " + ip);
		    ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//System.out.println("2 >> " + ip);
		    ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//System.out.println("3 >> " + ip);
		    ip = req.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//System.out.println("4 >> " + ip);
		    ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//System.out.println("5 >> " + ip);
		    ip = req.getRemoteAddr();
		}
		//System.out.println("final >> " + ip);

		return ip;
	}

	public static String enterContent(String data){
    String str = "";
    if(!StringUtil.isEmpty(data)){
      str = data.replaceAll("\r\n", "<br/>");
    }
    return str;
  }

  /**
   * HttpServletRequest의 파라미터값 map으로 바인딩
   * @param req
   * @return
   */
  public static Map<String, String> getParameters(HttpServletRequest req){

    Map<String,String> params = new HashMap<String, String>();
    Enumeration<String> en = req.getParameterNames();

    while(en.hasMoreElements()){
      String name = (String)en.nextElement();
      params.put(name, req.getParameter(name));
    }

    return params;
  }

  /**
   * @param str 원본 문자열
   * @param regEx 찾을 정규식
   * @param strToAdd 찾은 정규식 문자열 바로 다음에 더할 문자열
   * @return
   */
  public static String insertToEndThatFound(String str, String regEx, String strToAdd) {
    if (str == null) {return "";}
    StringBuilder sb = new StringBuilder(100);
    sb.append(str);
    System.out.println("str : " + str);
    int length = strToAdd.length();
    int insertLenth = 0;
    Matcher m = Pattern.compile(regEx).matcher(str);

    while (m.find()) {
      int start = m.end();
      sb.insert(start + insertLenth, strToAdd);
      System.out.println(sb);
      insertLenth += length;
    }

    return sb.toString();
  }

  public static int getByteToLenth(String value) {
    int length = 0;

    try {
      length = value.getBytes("EUC-KR").length;
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return length;
  }

  public static String convertCamelCase(String underScore) {

    if (underScore.indexOf('_') < 0
        && Character.isLowerCase(underScore.charAt(0))) {
        return underScore;
    }

    StringBuilder result = new StringBuilder();
    boolean nextUpper = false;
    int len = underScore.length();

    for (int i = 0; i < len; i++) {
        char currentChar = underScore.charAt(i);
        if (currentChar == '_') {
            nextUpper = true;
        } else {
            if (nextUpper) {
                result.append(Character.toUpperCase(currentChar));
                nextUpper = false;
            } else {
                result.append(Character.toLowerCase(currentChar));
            }
        }
    }
    return result.toString();
  }

  public static String convertUnderScore(String camelCase) {

    char firstWord = camelCase.charAt(0);

    if (Character.isUpperCase(firstWord)) return camelCase;

    StringBuilder result = new StringBuilder();
    int len = camelCase.length();
    char word;

    for (int i = 0; i < len; i++) {
      word = camelCase.charAt(i);

      if (Character.isUpperCase(word)) {
        result.append("_");
        result.append(word);
      } else {
        result.append(Character.toUpperCase(word));
      }

    }

    return result.toString();
  }
  
  /**
   * java object(vo)를 Map형태로 변환
   */
  public static Map<String,Object> convertObjectToMap(Object o){
    Map<String,Object> map = new HashMap<String,Object>();
    
    BeanWrapper temp = new BeanWrapperImpl(o);
    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(o.getClass());
    for (PropertyDescriptor pd : pds) {
      if (pd.getWriteMethod() != null) {
        map.put(pd.getName(), temp.getPropertyValue(pd.getName()));
      }
    }
    
    return map;
  }
  
  public static boolean isAjaxRequest(HttpServletRequest req) {
      if (req.getHeader("x-requested-with") != null || (req.getHeader("X-Requested-With") != null)|| req.getHeader("ORIGIN") != null) return true;
      
      return false;
  }
  public static Object convertMapToObject(Map<String,Object> map,Object obj){
	    String keyAttribute = null;
	    String setMethodString = "set";
	    String methodString = null;
	    Iterator itr = map.keySet().iterator();

	    while(itr.hasNext()){
	        keyAttribute = (String) itr.next();
	        methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
	        Method[] methods = obj.getClass().getDeclaredMethods();
	        for(int i=0;i<methods.length;i++){
	            if(methodString.equals(methods[i].getName())){
	                try{
	                    methods[i].invoke(obj, map.get(keyAttribute));
	                }catch(Exception e){
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    return obj;
	}
  
  /**
   * SWEETALERT PrintWriter로 사용하기
   * @param msg, function
   * @return
   */
  public static String swalPrintWriter(String msg){

       String swal = "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>"
              +"<html lang='ko'>"
              +"<head>"
              +"<script src='/common/js/jquery-1.11.3.min.js'></script>"
              +"<link rel='stylesheet' type='text/css' href='/common/js/modules/sweetalert/sweetalert.css'>"
              +"<script type='text/javascript' src='/common/js/modules/sweetalert/sweetalert-dev.js'></script>"
              +"<script type='text/javascript' src='/common/js/modules/jquery.alert.js'></script>"
              +"<meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head><body>"
              +"<script>Alert.info('"+msg+"');</script></body></html>";

      return swal;
  }
  public static String swalPrintWriter(String msg, String function){

       String  swal = "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>"
                  +"<html lang='ko'>"
                  +"<head>"
                  +"<script src='/common/js/jquery-1.11.3.min.js'></script>"
                  +"<link rel='stylesheet' type='text/css' href='/common/js/modules/sweetalert/sweetalert.css'>"
                  +"<script type='text/javascript' src='/common/js/modules/sweetalert/sweetalert-dev.js'></script>"
                  +"<script type='text/javascript' src='/common/js/modules/jquery.alert.js'></script>"
                  +"<meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head><body>"
                  +"<script>Alert.info('"+msg+"', function(){"+function+"});</script></body></html>";

      return swal;
  }
  
  /**
   * 금액 한글로 변환
   * @param money
   * @return
   */
  public static String convertMonetyToHangul(String money){
     if(money == null) return "";
     
     money = money.replace(",", "");
     
     String[] han1 = {"","일","이","삼","사","오","육","칠","팔","구"};
     String[] han2 = {"","십","백","천"};
     String[] han3 = {"","만","억","조","경"};

     StringBuffer result = new StringBuffer();
     int len = money.length();
     for(int i=len-1; i>=0; i--){
         result.append(han1[Integer.parseInt(money.substring(len-i-1, len-i))]);
         if(Integer.parseInt(money.substring(len-i-1, len-i)) > 0)
             result.append(han2[i%4]);
         if(i%4 == 0)
             result.append(han3[i/4]);
     }
     
     return result.toString();
 }
  public static boolean isEmail(String email) {
     if (email == null)
        return false;
     boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
     return b;
 }
  
  
  /*
   * 
   */
  public static String getConvertDateFormat(String date, String no){
		String rtn = "";
		String match = "[^0-9]";
	      
		if(date != null && !date.equals("")){
			date = date.replaceAll(match, "");
			
			String num = "";
			if(no != null && !no.equals("")) {
				if((no+"").length() < 4) {
					num = getConvertNumberFormat(no, "0", 3);
				} else {
					num = no + "";
				}
				
				if(date.length() == 8) {
					return date.substring(2,8) + (num == "" ? "" : "-"+num);
				} else if(date.length() == 6) {
					return date + (num == "" ? "" : "-"+num);
				}
			} else {
				if(date.length() == 8) {
					return date.substring(2,8) + (num == "" ? "" : "-"+num);
				} else if(date.length() == 6) {
					return date + (num == "" ? "" : "-"+num);
				}
			}
			
		}
		return rtn;
  }

  public static String getConvertNumberFormat(String no, String ch, int len) {
	  
	  String result = "";
	  int noLength = no.length();
	  if( noLength < len && !ch.equals("") ) {
		  for(int i=0; i<len-noLength; ++i) {
			  result += ch;
		  }
	  }
	  result += no;
	  return result;
  }
  

  public static String getIdxNo(String procGbn,String procDt, String procNo) {
      
      String result = "";
      procGbn = convNull(procGbn, "");  
      procDt = convNull(procDt, "");  
      procNo = convNull(procNo, "");
      
      if(!procGbn.equals("") && !procDt.equals("") && !procNo.equals("")) {
      result = procGbn + procDt.substring(2, 6);
      
      String no = "";
      if((procNo+"").length() < 4) {
         no = getConvertNumberFormat(procNo, "0", 3);
     } else {
         no = procNo + "";
     }
      result += "-"+no;
      }
      return result;
  }
  
  public static String getChkProcDt(String date) {
     String result = "";
     date = convNull(date, "");
     if(!date.equals("")) {
        result = date.substring(2, 6);
     }
     return result;
  }
  
  // 엑셀  수량 및 단가 convert 지수로 나와서 엑셀 출력 시 깨짐 보완 --20210726 ksh
  public static String getConvertQty(Object num, String type) {
     String result = "";
     num = convNull(num, "");
     if(!num.equals("")) {
        if(type.equals("qty")) {
           DecimalFormat format = new DecimalFormat("###,###.0000");
           result = convertString(num);
           if(result.indexOf("E") > -1) {
              int exp = Integer.parseInt(result.substring(result.indexOf("E") + 1, result.length()));
              double expNum = Double.parseDouble(result.substring(0 ,result.indexOf("E") - 1));
              double expResult = 0;
              double expDouble = 1;
              if(exp < 0) {
                 for(int i = exp; i < 0; i++) {
                    expDouble = expDouble * 0.1;
                 }
              }
              else if(exp > 0) {
                 for(int i = exp; i > 0; i--) {
                    expDouble = expDouble * 10;
                 }
              }
              expResult = expNum * expDouble;
              BigDecimal bd = new BigDecimal(expResult); 
              result = convertString(bd);
           }
           else
           result = convertString(Math.round(Double.parseDouble(result) * 10000)/ 10000.0);
        }
        else if(type.equals("price")) {
           result = convertString(num);
           result = convertString(Math.round(Double.parseDouble(result)));
        }
     }
   
     return result;
  }
  
  
  public static String getProjectCode(String pjDate, String pjNo) {
      
      String result = "";
   
      pjDate = convNull(pjDate, "");  
      pjNo = convNull(pjNo, "");
      
      if(!pjDate.equals("") && !pjNo.equals("")) {
      result = "PJ-" + pjDate;
      
      String no = "";
      if((pjNo+"").length() < 4) {
         no = getConvertNumberFormat(pjNo, "0", 3);
     } else {
         no = pjNo + "";
     }
      result += "-"+no;
      }
      return result;
  }
  
  public static String createGoodsCode(String date, String no) {
	  
	  String result = "";
	  
	  date = convNull(date, "");  
	  no = convNull(no, "");
	  
	  if(!date.equals("") && !date.equals("")) {
		  result = "P" + date;
		  
		  String temp = "";
		  if((no+"").length() < 4) {
			  temp = getConvertNumberFormat(no, "0", 3);
		  } else {
			  temp = no + "";
		  }
		  result += temp;
	  }
	  
	  return result;
  }
  
  
  
}
