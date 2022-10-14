package com.kph.mate.common.util;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 문자열 
 */
public class StringUtils  {
 
	 private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

	public static String defaultValue(Object value){
		return value==null?"":value.toString();
	}

	public static String defaultValue(Object value, String defaultValue){
		return value==null?defaultValue:value.toString();
	}
	public static String defaultValue(Object value, String[][] defaultValue){
		if(value==null){
			return "";
		}
		for(String[] s : defaultValue){
			if(value.toString().equals(s[0])){
				return s[1].trim();
			};
		}
		return "";
	}
	public static String defaultValue(Object value, HashMap<String,Object> defaultValue){
		if(value==null){
			return "";
		}
		for (Map.Entry<String,Object> entry : defaultValue.entrySet()){
			if(value.toString().equals(entry.getKey())){
				return entry.getValue().toString();
			}
		}
		return "";
	}
	
	/**
	 * 값 유무 확인
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		if (str == null) {
			return true;
		}
		return str.toString().trim().equals("")?true:false;
	}
	public static boolean isEmpty(Object[] str) {
		if (str == null) {
			return true;
		}else if(str.length==0){
			return true;
		}
		return str.toString().trim().equals("")?true:false;
	}
	


	/**
	 * 위도,경도 소숫점 1plus
	 * @param str
	 * @return
	 */
	public static String convertLatLng(String str) {
		try {
			int io = str.indexOf(".");
			int lnt = str.substring(io).length();

			String tmp = "0.";
			for (int i = 0; i < lnt - 1; i++) {
				if (i != lnt - 2) {
					tmp += "0";
				} else {
					tmp += "1";
				}
			}
			BigDecimal b1 = new BigDecimal(str);
			BigDecimal b2 = new BigDecimal(tmp);
			str = b1.add(b2).toString();
		} catch (Exception e) {
			return str;
		}
		return str;
	}

	/**
	 * int 문자에서 원하는값 추출
	 * @param str
	 * @param ext
	 * @return
	 */
	public static int extractStr(String str, String ext) {
		int result = 0;
		String[] strArr = str.split("&");
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].indexOf(ext) > -1) {
				String[] arr = strArr[i].split("=");
				result = Integer.parseInt(arr[1]);
				break;
			}
		}
		return result;
	}

	/**
	 * 모든 태그 삭제
	 * @param str
	 * @return
	 */
	public static String removeAllTag(String str) {
		String rs = str;
		if (rs == null)
			return rs;
		rs = str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>",
				"");
		return rs;
	}

	/**
	 * 모든 특수문자 제거
	 * @param str
	 * @return
	 */
	public static String removeAllStr(String str) {
		String str_imsi = "";
		String[] filter_word = { "", "\\.", "\\?", "\\/", "\\~", "\\!", "\\@",
				"\\#", "\\$", "\\%", "\\^", "\\&", "\\*", "\\(", "\\)", "\\_",
				"\\+", "\\=", "\\|", "\\\\", "\\}", "\\]", "\\{", "\\[",
				"\\\"", "\\'", "\\:", "\\;", "\\<", "\\,", "\\>", "\\.", "\\?",
				"\\/" };
		for (int i = 0; i < filter_word.length; i++) {
			str_imsi = str.replaceAll(filter_word[i], "");
			str = str_imsi;
		}
		return str;
	}

	/**
	 * 모든 공백 제거
	 * @param str
	 * @return
	 */
	public static String removeAllSpace(String str) {
		str = str.replaceAll("\\p{Space}", "");
		return str;
	}



	/**
	 * String 을 Integer 변환
	 * @param val
	 * @return
	 */
	public static int toInt(Object obj) {
		if(obj==null)
			return 0;
		String val = String.valueOf(obj);
		int results = 0;
		if (val == null || val.trim().equals("")) {
			return 0;
		}
		try {
		    val = val.replaceAll(",", "");
			results = Integer.parseInt(val.trim());
		} catch (Exception e) {
			results = 0;
		}
		return results;
	}

	/**
	 * char 을 Float 변환
	 * @param c
	 * @return
	 */
	public static float toFloat(char c) {
		return toFloat(c + "");
	}
	public static float toFloat(Object o) {
		return toFloat(String.valueOf(o));
	}

	/**
	 * String 을 Float 변환
	 * @param val
	 * @return
	 */
	public static float toFloat(String val) {
		float results = 0;
		if (val == null || val.equals("null") || val.trim().equals("")) {
			return 0;
		}
		try {
			results = Float.parseFloat(val);
		} catch (Exception e) {
			e.printStackTrace();
			results = 0;
		}
		return results;
	}


	/**
	 * char 을 Long 변환
	 * @param c
	 * @return
	 */
	public static long toLong(char c) {
		return toLong(c + "");
	}

	/**
	 * String 을 Long 변환
	 * @param val
	 * @return
	 */
	public static long toLong(String val) {
		long results = 0;
		if (val == null || val.trim().equals("")) {
			return 0;
		}
		try {
			results = Long.valueOf(val).longValue();
		} catch (Exception e) {
			results = 0;
		}
		return results;
	}


	/**
	 * char 을 Double 변환
	 * @param c
	 * @return
	 */
	public static double toDouble(char c) {
		return toDouble(c + "");
	}

	/**
	 * String  을 Double 변환
	 * @param val
	 * @return
	 */
	public static double toDouble(String val) {
		double results = 0;
		if (val == null || val.trim().equals("")) {
			return 0;
		}
		try {
			results = Double.valueOf(val).doubleValue();
		} catch (Exception e) {
			results = 0;
		}
		return results;
	}

	//

	/**
	 *  A지점에서 B지점 거리 계산 (GPS 위도, 경도 )<br> *단위:M
	 * @param pointA  {latitude,longitude}
	 * @param pointB   {latitude,longitude}
	 * @return
	 */
	public static long distance(HashMap<String, Object> pointA,
			HashMap<String, Object> pointB) {
		double P1_latitude = 0;
		double P1_longitude = 0;
		double P2_latitude = 0;
		double P2_longitude = 0;
		if (pointA == null || pointB == null) {
			return 0;
		}
		if (pointA.get("latitude") != null) {
			P1_latitude = toDouble(pointA.get("latitude").toString());
		}
		if (pointA.get("longitude") != null) {
			P1_longitude = toDouble(pointA.get("longitude").toString());
		}
		if (pointB.get("latitude") != null) {
			P2_latitude = toDouble(pointB.get("latitude").toString());
		}
		if (pointB.get("longitude") != null) {
			P2_longitude = toDouble(pointB.get("longitude").toString());
		}

		if (pointA.get("lat") != null) {
			P1_latitude = toDouble(pointA.get("lat").toString());
		}
		if (pointA.get("lon") != null) {
			P1_longitude = toDouble(pointA.get("lon").toString());
		}
		if (pointB.get("lat") != null) {
			P2_latitude = toDouble(pointB.get("lat").toString());
		}
		if (pointB.get("lon") != null) {
			P2_longitude = toDouble(pointB.get("lon").toString());
		}
		return distance(P1_latitude, P1_longitude, P2_latitude, P2_longitude);
	}

	/**
	 * A지점에서 B지점 거리 계산 (GPS 위도, 경도 )<br> *단위:M
	 * @param pointA  {latitude,longitude}
	 * @param pointB  {latitude,longitude}
	 * @return
	 */
	public static long distance(double[] pointA, double[] pointB) {
		if (pointA.length != 2 || pointB.length != 2) {
			return 0;
		}
		return distance(pointA[0], pointA[1], pointB[0], pointB[1]);
	}

	/**
	 * A지점에서 B지점 거리 계산 (GPS 위도, 경도 )<br> *단위:M
	 * @param P1_latitude
	 * @param P1_longitude
	 * @param P2_latitude
	 * @param P2_longitude
	 * @return
	 */
	public static long distance(String P1_latitude, String P1_longitude,
			String P2_latitude, String P2_longitude) {
		return distance(toDouble(P1_latitude), toDouble(P1_longitude),
				toDouble(P2_latitude), toDouble(P2_longitude));
	}

	/**
	 * A지점에서 B지점 거리 계산 (GPS 위도, 경도 )<br> *단위:M
	 * @param P1_latitude
	 * @param P1_longitude
	 * @param P2_latitude
	 * @param P2_longitude
	 * @return
	 */
	public static long distance(double P1_latitude, double P1_longitude,
			double P2_latitude, double P2_longitude) {
		if ((P1_latitude == P2_latitude) && (P1_longitude == P2_longitude)) {
			return 0;
		}
		double e10 = P1_latitude * Math.PI / 180;
		double e11 = P1_longitude * Math.PI / 180;
		double e12 = P2_latitude * Math.PI / 180;
		double e13 = P2_longitude * Math.PI / 180;

		double c16 = 6356752.314140910;
		double c15 = 6378137.000000000;
		double c17 = 0.0033528107;

		double c18 = e13 - e11;
		double c20 = (1 - c17) * Math.tan(e10);
		double c21 = Math.atan(c20);
		double c22 = Math.sin(c21);
		double c23 = Math.cos(c21);
		double c24 = (1 - c17) * Math.tan(e12);
		double c25 = Math.atan(c24);
		double c26 = Math.sin(c25);
		double c27 = Math.cos(c25);

		double c29 = c18;
		double c31 = (c27 * Math.sin(c29) * c27 * Math.sin(c29))
				+ (c23 * c26 - c22 * c27 * Math.cos(c29))
				* (c23 * c26 - c22 * c27 * Math.cos(c29));
		double c33 = (c22 * c26) + (c23 * c27 * Math.cos(c29));
		double c35 = Math.sqrt(c31) / c33;
		double c38 = 0;
		if (c31 == 0) {
			c38 = 0;
		} else {
			c38 = c23 * c27 * Math.sin(c29) / Math.sqrt(c31);
		}

		double c40 = 0;
		if ((Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))) == 0) {
			c40 = 0;
		} else {
			c40 = c33 - 2 * c22 * c26
					/ (Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38)));
		}

		double c41 = Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))
				* (c15 * c15 - c16 * c16) / (c16 * c16);
		double c43 = 1 + c41 / 16384
				* (4096 + c41 * (-768 + c41 * (320 - 175 * c41)));
		double c45 = c41 / 1024 * (256 + c41 * (-128 + c41 * (74 - 47 * c41)));
		double c47 = c45
				* Math.sqrt(c31)
				* (c40 + c45
						/ 4
						* (c33 * (-1 + 2 * c40 * c40) - c45 / 6 * c40
								* (-3 + 4 * c31) * (-3 + 4 * c40 * c40)));

		return Math.round((c16 * c43 * (Math.atan(c35) - c47)));
	}


	/**
	 * A지점에서 B지점 거리 계산 (GPS 위도, 경도 )<br> *단위:M
	 * @param P1_latitude
	 * @param P1_longitude
	 * @param P2_latitude
	 * @param P2_longitude
	 * @return
	 */
	public static int distanceInt(String P1_latitude, String P1_longitude,
			String P2_latitude, String P2_longitude) {
		return distanceInt(toDouble(P1_latitude), toDouble(P1_longitude),
				toDouble(P2_latitude), toDouble(P2_longitude));
	}

	/**
	 * A지점에서 B지점 거리 계산 (GPS 위도, 경도 )<br> *단위:M
	 * @param P1_latitude
	 * @param P1_longitude
	 * @param P2_latitude
	 * @param P2_longitude
	 * @return
	 */
	public static int distanceInt(double P1_latitude, double P1_longitude,
			double P2_latitude, double P2_longitude) {
		if ((P1_latitude == P2_latitude) && (P1_longitude == P2_longitude)) {
			return 0;
		}
		double e10 = P1_latitude * Math.PI / 180;
		double e11 = P1_longitude * Math.PI / 180;
		double e12 = P2_latitude * Math.PI / 180;
		double e13 = P2_longitude * Math.PI / 180;

		double c16 = 6356752.314140910;
		double c15 = 6378137.000000000;
		double c17 = 0.0033528107;

		double c18 = e13 - e11;
		double c20 = (1 - c17) * Math.tan(e10);
		double c21 = Math.atan(c20);
		double c22 = Math.sin(c21);
		double c23 = Math.cos(c21);
		double c24 = (1 - c17) * Math.tan(e12);
		double c25 = Math.atan(c24);
		double c26 = Math.sin(c25);
		double c27 = Math.cos(c25);

		double c29 = c18;
		double c31 = (c27 * Math.sin(c29) * c27 * Math.sin(c29))
				+ (c23 * c26 - c22 * c27 * Math.cos(c29))
				* (c23 * c26 - c22 * c27 * Math.cos(c29));
		double c33 = (c22 * c26) + (c23 * c27 * Math.cos(c29));
		double c35 = Math.sqrt(c31) / c33;
		double c38 = 0;
		if (c31 == 0) {
			c38 = 0;
		} else {
			c38 = c23 * c27 * Math.sin(c29) / Math.sqrt(c31);
		}

		double c40 = 0;
		if ((Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))) == 0) {
			c40 = 0;
		} else {
			c40 = c33 - 2 * c22 * c26
					/ (Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38)));
		}

		double c41 = Math.cos(Math.asin(c38)) * Math.cos(Math.asin(c38))
				* (c15 * c15 - c16 * c16) / (c16 * c16);
		double c43 = 1 + c41 / 16384
				* (4096 + c41 * (-768 + c41 * (320 - 175 * c41)));
		double c45 = c41 / 1024 * (256 + c41 * (-128 + c41 * (74 - 47 * c41)));
		double c47 = c45
				* Math.sqrt(c31)
				* (c40 + c45
						/ 4
						* (c33 * (-1 + 2 * c40 * c40) - c45 / 6 * c40
								* (-3 + 4 * c31) * (-3 + 4 * c40 * c40)));

		return (int) Math.round((c16 * c43 * (Math.atan(c35) - c47)));
	}

	/**
	 * 메세지 문장 치환
	 * @param keyDatas
	 * @param message
	 * @return
	 */
	public static String getMessage(HashMap<String, Object> keyDatas,
			String message) {
		if (keyDatas != null && message != null) {
			Map<String, Object> map = keyDatas;
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Object objValue = map.get(key);
				String strValue = "";
				if (objValue != null)
					strValue = objValue.toString().trim();
				if ("gem".equals(key))
					strValue = strValue.toLowerCase();
				message = message.replaceAll("#" + key + "#", strValue);
			}
		}
		return message;
	}



	/**
	 * 문자 케리터 확인
	 * @param str_kr
	 * @throws UnsupportedEncodingException
	 */
	public static void charOut(String str_kr) throws UnsupportedEncodingException{
        String charset[] = {"euc-kr", "ksc5601", "iso-8859-1", "8859_1", "ascii", "UTF-8","latin1"};

        for(int i=0; i<charset.length ; i++){
            for(int j=0 ; j<charset.length ; j++){
                if(i==j)
                 continue;
//                System.out.println(charset[i]+" : "+charset[j]+" :"+new String(str_kr.getBytes(charset[i]),charset[j]));
                logger.info(charset[i]+" : "+charset[j]+" :"+new String(str_kr.getBytes(charset[i]),charset[j]));
            }
        }
    }

	  /**
	   *  숫자 천자리 콤마 (*소수점 제외)
	 * @param val
	 * @return
	 */
	public static String CommaIntger(String val){
		  return CommaNumber(val, "###,###,###,###,##0");
	  }
    /**
     * 숫자 천자리 콤마
     * @param val
     * @return
     */
    public static String CommaNumber(String val){
        return CommaNumber(val, "###,###,###,###,##0.00");
    }
	/**
	 * 숫자 천자리 콤마
	 * @param val
	 * @param decimalFormat
	 * @return
	 */
    public static String CommaNumber(String val, String decimalFormat ){
        if(val==null || "".equals(val))
        {
             return "0";
        }
        Double doubleAmount = Double.valueOf(val.replaceAll(",", ""));
        double myAmount = doubleAmount.doubleValue();
        NumberFormat f = new DecimalFormat(decimalFormat);
        String s = f.format(myAmount);
        return s;
    }

    /**
     *  퍼센트로 값구하기
     * @param total
     * @param value
     * @return
     */
    public static String percentToValue(String total, String value){
   	 try{
   		 Double doubleTotal = Double.valueOf(total);
   		 Double doubleValue = Double.valueOf(value);
            double t = doubleTotal.doubleValue();
            double v = doubleValue.doubleValue();
            NumberFormat f = new DecimalFormat("###,###.###");
            return f.format((t*v)/100);
   	 }catch(Exception e){
   		 return "0";
   	 }
    }

    /**
     * 값으로 퍼센트구하기
     * @param total
     * @param value
     * @return
     */
    public static String valueToPercent(String total, String value){
   	 try{
   		 Double doubleTotal = Double.valueOf(total);
   		 Double doubleValue = Double.valueOf(value);
            double t = doubleTotal.doubleValue();
            double v = doubleValue.doubleValue();
            NumberFormat f = new DecimalFormat("###,###.##");
            return f.format((v/t)*100);
   	 }catch(Exception e){
   		 return "0";
   	 }
    }
    
    
    public static String getNumberFormat(Object val){
    	String result = "";
    	if(val!=null){
    		Pattern pattern = Pattern.compile("\\d");
    		Matcher matcher = pattern.matcher(String.valueOf(val).replaceAll(" ", "").replaceAll("-", "").replaceAll("\\)", "").trim());
    		while(matcher.find()){
    			result += matcher.group(0);
    		}
    	}
    	return result.trim();
    }
    /**
     * 전화번호 페턴 으로 치환
     * @param val
     * @return
     */
    public static String getPhoneFormat(String val){
    	String result = "";
    	if(val!=null){
    		Pattern pattern = Pattern.compile("\\d");
    		Matcher matcher = pattern.matcher(val.replaceAll(" ", "").replaceAll("-", "").replaceAll("\\)", "").trim());
    		while(matcher.find()){
    			result += matcher.group(0);
    		}
    		if(result.startsWith("02")){
    			result = result.replaceFirst("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    		}else{
    			if(val.length()<10){
    				result = result.replaceFirst("(\\d{3,4})(\\d{4})", "$1-$2");
    			}else{
    				result = result.replaceFirst("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    			}
    		}
    	}
    	return result.trim();
    }

    /**
     * 배열 문자 합치기
     * @param array
     * @return
     */
    public static String join(String[] array){
    	return join(array,"");
    }


	/**
	 *  배열 문자 합치기
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String join(String[] array, String separator){

		String result = "";
		boolean comma = false;

		if(isEmpty(separator)){
			separator = ",";
		}

		if(array != null){
			for(String s : array){
				if(comma){
					result += separator;
				}
				result += s;
				comma = true;
			}
		}

		return result.trim();
	}


	public static String getContextPath(HttpServletRequest request) {
		String result = "/";
		try {
			String[] s = request.getRequestURI().split("/");
			for (int i = 0; i < s.length; i++) {
				if (i < s.length - 1) {
					result += s[i] + "/";
				}
			}
			result = result.replaceAll("//", "/").replaceAll("//", "/");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return result;
	}

	public static String getViewPageEtx(HttpServletRequest request){
		return "."+(request.getAttribute("viewPageExt")==null?"page":(String)request.getAttribute("viewPageExt"));
	}

	public static String getPath(String file){
		String[] s = file.split("");
		String path = "";
		for(int i=0;i<s.length-1;i++){
			path += "/"+s[i];
		}
		return path;
	}

	/**
	 * 숫자 랜덤
	 * @param length
	 * @return
	 */
	public static String mathRandomNumber(int length){
		return mathRandom("0123456789",length);
	}

	/**
	 * 문자 랜덤
	 * @param length
	 * @return
	 */
	public static String mathRandomString(int length){
		return mathRandom("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",length);
	}

	/**
	 * 특정 문자 랜덤
	 * @param chars
	 * @param length
	 * @return
	 */
	public static String mathRandom(String chars, int length){
		StringBuilder sb = new StringBuilder( length );
		   for( int i = 0; i < length; i++ )
		      sb.append( chars.charAt( (int) (Math.random() * chars.length()) ));
		   return sb.toString();
	}
	
	/**
	 * URL 엔코딩
	 * 
	 * @param origin
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptURL(String origin) throws UnsupportedEncodingException {
		return URLEncoder.encode(origin, "utf-8").replace("+", "%20").replace("*", "%2A").replace("~", "%7E");
	}

	/**
	 * URL 디코딩
	 */
	public static String decryptURL(String origin) throws UnsupportedEncodingException {
		return URLDecoder.decode(origin, "utf-8").replace("%20", "+").replace("%2A", "*").replace("%7E", "~");
	}

	/**
	 * 유니코드
	 * @param unicode
	 * @return
	 * @throws Exception
	 */
	public static String decodeUnicode(String unicode) throws Exception {
		StringBuffer str = new StringBuffer();
		char ch = 0;
		for (int i = unicode.indexOf("\\u"); i > -1; i = unicode.indexOf("\\u")) {
			ch = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
			str.append(unicode.substring(0, i));
			str.append(String.valueOf(ch));
			unicode = unicode.substring(i + 6);
		}
		str.append(unicode);

		return str.toString();
	}

	/**
	 * 유니코드
	 * @param unicode
	 * @return
	 * @throws Exception
	 */
	public static String encodeUnicode(String unicode) throws Exception {
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < unicode.length(); i++) {
			if ((unicode.charAt(i) == 32)) {
				str.append(" ");
				continue;
			}
			str.append("\\u");
			str.append(Integer.toHexString(unicode.charAt(i)));
		}
		return str.toString();

	}
	
	public static String zeroFormat(Object value, int length){
		if(value==null)
			return "";
		try{
			String str = String.valueOf(value);
			if(str.length() < length){
				int len = str.length();
				for(int i=0;i<(length-len);i++){
					str = "0"+str;
				}
			}
			return str;
		}catch(Exception e){
			System.err.println(e);
		}
		return String.valueOf(value);
	}
	

}
