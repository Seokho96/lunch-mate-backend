package com.kph.mate.common.crypto;

import com.kph.mate.common.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Seeding {

	/**
	 * ECB Mode 테스트
	 * 	- ECB 모드는 초기화백터(IV) 없음
	 * 	- Padding 은 X923Padding, PKCSPadding 사용
	 * 	- 암호화 값의 Encoding 방식은 3가지 (Base64, UrlBase64, Hex)
	 * 		-> Base64 : Base64 방식 인코딩 - 웹 파라미터 전달시 "+" 문자로 인해 공백으로 전달됨.
	 * 			받을때 "+" 변경 필요. 또는 UrlEncoding 활용
	 * 		-> UrlBase64 : Base64 에서 특수문자를 다른 문자형식으로 변경(Base64 UrlEncoding 과 유사)
	 * 		-> HEX : Byte HEX 값으로 인코딩 ("0x??" 방식이 아니고 ?? 방식으로 인코딩 : 0x01 => 01)
	 *
	 */
	private static String Aprocni_Key = "f2f49sdfjos0te2t";    // 16자리 준수
	private static String Aprocni_SubKey = "1242457826";    // 서브키
	public static int Useing_Hour = 25; 						// 복화 유효 사용 시간
	private static int[] Cross_Key = new int[]{9,7,5,3,2};			// 순서 섞는 단위
	/**
	 * 현재 시간에서 시간을 더한 시간 추출  함수
	 * @param hour 현재 시간
	 * @return
	 */
	private static String getDateTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(cal.getTime());
	}

	private static String getReplace(String data,boolean bool){
		String r_data = data;
		if (bool){
			r_data = r_data.replace("=", "_");
			r_data = r_data.replace("+", "^");
			r_data = r_data.replace("/", "*");
		}else{
			r_data = r_data.replace("_", "=");
			r_data = r_data.replace("^", "+");
			r_data = r_data.replace( "*","/");
		}
		return r_data;
	}
	/**
	 * 암호화 값 시간 유효 체크 함수
	 * @param CaheckDate : 복화한 시간 값
	 * @return true : false
	 * @throws java.text.ParseException
	 */
	private static boolean getTimeUseCheck(String CaheckDate){
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		long elapse = 0;
		long Useing_Time = Useing_Hour * 60 * 60;
		//System.out.println("DATE===>"+CaheckDate);
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			cal.setTime(formatter.parse(CaheckDate));
		}catch(Exception e){
			return false;
		}
		elapse = (cal2.getTimeInMillis() - cal.getTimeInMillis()) / 1000;
		if (elapse > Useing_Time){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 암호화 값 시간 유효 체크 함수
	 * @param CaheckDate : 복화한 시간 값
	 * @return true : false
	 * @throws java.text.ParseException
	 */
	private static boolean getTimeUseCheck(String CaheckDate, int minutes){
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		long elapse = 0;
		long Useing_Time = minutes * 60;
		//System.out.println("DATE===>"+CaheckDate);
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			cal.setTime(formatter.parse(CaheckDate));
		}catch(Exception e){
			return false;
		}
		elapse = (cal2.getTimeInMillis() - cal.getTimeInMillis()) / 1000;
		if (elapse > Useing_Time){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 문자열 길이를 문자로 반환 함수
	 * @param data 문자열
	 * @return
	 */
	private static String getLength(String data)	{
		int len = data.length();
		return (len>9)? Integer.toString(len):"0"+ Integer.toString( data.length());
	}
	/**
	 * 배열 순서 재 배치 함수
	 * @param indata 문자열
	 * @return
	 */
	private static String Cross(String indata){
		char[] data = new char[indata.length()];
		char temp;
		int step = 0;
		String return_value ;
		for (int i=0;i< indata.length();i++) data[i] = indata.charAt(i);
		for(int j = 0; j < Cross_Key.length; j++){
			step = Cross_Key[j];
			if (0 == step) step = 5;
			if (1 == step) step = 9;
			for(int i = 0; i < data.length; i = i + step){
				temp = data[0];
				data[0] = data[i];
				data[i] = temp;
			}
		}

		return_value = new String(data);
//		System.out.println("return_value==>"+ return_value);
		return return_value;
	}
	/**
	 * 배열 순서 복화 함수
	 * @param indata 문자열
	 * @return
	 */
	private static String UnCross(String indata){
		char[] data = new char[indata.length()];
		char temp;
		String return_value ;
		int startpointer = 0;
		int unstep = 0;
		for (int i=0;i< indata.length();i++) data[i] = indata.charAt(i);
		for(int j = Cross_Key.length-1; j >= 0 ; j--){
			unstep = Cross_Key[j];
			if (0 == unstep) unstep = 5;
			if (1 == unstep) unstep = 9;
			startpointer = (int)(Cross_Key[j] * (Math.floor((data.length-1) / unstep)));
			int step = unstep;
			for(int i = startpointer; i  >= step; i = i - step){
				temp = data[i];
				data[i] = data[0];
				data[0] = temp;
			}
		}
		return_value = new String(data);
//		System.out.println("return_value==>"+ return_value);
		return return_value;
	}
	/**
	 * 문자열의 숫자 여부 체크 함수
	 * @param str 문자열
	 * @return
	 */
	private static boolean NumberChk(String str){
		char c = 0;
		if (str.equals("")) return false;
		for (int i=0; i < str.length(); i++){
			c = str.charAt(i);
			if (c < 48 || c > 59) return false;
		}
		return true;
	}

	public static String seeding(String data, String seedMode) throws Exception {
	  return seeding(data, seedMode, false);
	}
	/**
	 * 복화 가능한 시간 제한 을 가지고 있는 암호화 함수
	 * @param data : 암/복호화 할 데이터
	 * @param seedMode : 암/복화 구분 (ENC,DEC)
	 * @return
	 * @throws Exception
	 */
	public static String seeding(String data, String seedMode, boolean isIncludingDate) throws Exception {

		int[] seedKey = SEEDUtil.getSeedRoundKey(Aprocni_Key);

		String outString = null;
		if (data == null || data==""){
			//System.out.println("["+seedMode+"]data null처리");
			return "";
		}
		//System.out.println("[SEED]->"+data);
		if (seedMode.equals("ENC")) {
			//암호화
			String encTime = SEEDUtil.getSeedEncrypt(getDateTime(), seedKey);// 현재 시간 값 암호화
			String encStr = SEEDUtil.getSeedEncrypt(data, seedKey);//기본 Base64 인코딩 값
			String return_data = encTime + encStr + getLength(encTime);			// 생성 시간[yyyyMMddHHmmss] + 암호화 값 + 생성시간  길이
			return_data = getReplace(return_data, true);

			outString = Cross(return_data);										// 순서 재 배치 하기
			//System.out.println("[ENC]->"+outString);
		} else if (seedMode.equals("DEC")) {
			//복호화
			String Undata = UnCross(data);										// 배열 순서 복화 하기
			Undata = getReplace(Undata, false);
//			System.out.println("data->"+data);
			String timelength = Undata.substring(Undata.length()-2,Undata.length());		// 생성시간문자열 길이 추출
			String decTime = "";
			String decData = "";
			if (!NumberChk(timelength) || Integer.parseInt(timelength) >= Undata.length()) {											//  숫자 여부 검증
				//System.out.println("SEED[DEC] = Type Error");
				return null;
			}
			decTime = Undata.substring(0,Integer.parseInt(timelength));				// 시간 값 추출
			decData = Undata.substring(Integer.parseInt(timelength), Undata.length()-2);  // 암호화 데이터 추출
			//decTime = Crypt.decrypt(decTime);									// 시간 값 복화
			decTime = SEEDUtil.getSeedDecrypt(decTime, seedKey);
			if (!getTimeUseCheck(decTime)){											// 유효 시간 검증
				//System.out.println("SEED[DEC] = Useing Time Over");
				return null;
			}

			String decStr = SEEDUtil.getSeedDecrypt(decData, seedKey);

			if (isIncludingDate) {
        outString = decTime;
      }

			outString += decStr;
//			System.out.println("outString : " + outString);

			//System.out.println("[DEC]->"+outString);
		}

//		System.out.println("seedMode = " + seedMode);
//		System.out.println("OutData = " + outString);

		return outString;

	}
	/**
	 * 고정 값 암/복호화 함수
	 * @param data 암/복호화 대상값
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return
	 */
	public static String seeding_always(String data, String seedMode) {
		return seeding(data, seedMode, Aprocni_Key);

	}

	/**
	 * 데이터 암호화
	 * @param data
	 * @return
	 */
    public static String seeding_always_enc(String data) {
        return seeding(data, "ENC", Aprocni_Key);

    }

    /**
     * 데이터 복호화
     * @param data
     * @return
     */
    public static String seeding_always_dec(String data) {
        return seeding(data, "DEC", Aprocni_Key);

    }
    
	/**
	 * 암/복호화 함수
	 * @param data 암/복호화 대상 데이터
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @param _key 암/복호화 키
	 * @return
	 */
	public static String seeding(String data, String seedMode, String _key) {
		String outString = null;

		try{
			int[] seedKey = SEEDUtil.getSeedRoundKey(Aprocni_Key);

			if (data == null){
				//System.out.println("["+seedMode+"]data null처리");
				return "";
			}
			if (data.equals("")){
			//	System.out.println("["+seedMode+"]data null처리");
				return "";
			}
		
			if (seedMode.equals("ENC")) {
				//암호화
//				final Logger logger = Logger.getLogger(Aprocni_Seeding.class);
//				logger.info("##################################1 : " + _key.getBytes()[0]);
//				logger.info("##################################2 : " + _pad);
//				logger.info("##################################3 : " + _mode);
				String encStr = SEEDUtil.getSeedEncrypt(data, seedKey);
				encStr = getReplace(encStr, true);

				outString = encStr;

			} else if (seedMode.equals("DEC")) {
				//복호화
				data = getReplace(data, false);
				String decStr = SEEDUtil.getSeedDecrypt(data, seedKey);

				outString = decStr;
			}
		} catch(Exception e){
			outString = "";
		}

		//System.out.println("seedMode = " + seedMode);
		//System.out.println("OutData = " + outString);

		return outString;

	}
	/**
	 * 외부 제공용 암/복호화
	 * @param data 암/복호화 대상 데이터
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @param _key 암/복호화 키
	 * @return
	 * @throws Exception
	 */
	public static String seeding_API(String data, String seedMode, String _key) throws Exception {
		int[] seedKey = SEEDUtil.getSeedRoundKey(Aprocni_Key);

		if (data == null){
//			System.out.println("["+seedMode+"]data null처리");
			return "";
		}
		String outString = null;


		if (seedMode.equals("ENC")) {
			//암호화
			String encStr = SEEDUtil.getSeedEncrypt(data, seedKey);

			outString = encStr;

		} else if (seedMode.equals("DEC")) {
			//복호화
			String decStr = SEEDUtil.getSeedDecrypt(data, seedKey);

			outString = decStr;
		}

		//System.out.println("seedMode = " + seedMode);
		//System.out.println("OutData = " + outString);

		return outString;

	}


	/**
	 * Map타입의 데이터 암/복호화
	 * @param data 암/복호화 대상 map
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 map
	 */
	public static HashMap<String, Object> convertSeedingMapString(HashMap<String, Object> map, String[] key, String seedMode ){
		for(int k=0; k<key.length; k++){
			String value = (String)map.get(key[k]);
			//String temp=Aprocni_Seeding.seeding_always(value, "DEC");
			String temp = seeding_always(value, seedMode);
			if(temp.equals("") && seedMode=="DEC") // 복호화시 공백이 나오면 평문 전달
				temp = value;

			map.put(key[k], temp);/*
			if(!value.equals("null")){
				if(temp.equals("")){
					map.put(key[k], seeding_always(value, seedMode));
				}
			}else if(value.equals("null")){
				map.put(key[k], "");
			}*/
		}

		return map;
	}
	/**
	 * Map타입의 데이터 암/복호화
	 * @param data 암/복호화 대상 map
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 map
	 */
	public static Map<String, String> convertSeedingMapString(Map<String, String> map, String[] key, String seedMode ){
		for(int k=0; k<key.length; k++){
			String value = (String)map.get(key[k]);
			//String temp=Aprocni_Seeding.seeding_always(value, "DEC");
			String temp = seeding_always(value, seedMode);
			if(temp.equals("") && seedMode=="DEC") // 복호화시 공백이 나오면 평문 전달
				temp = value;

			map.put(key[k], temp);/*
			if(!value.equals("null")){
				if(temp.equals("")){
					map.put(key[k], seeding_always(value, seedMode));
				}
			}else if(value.equals("null")){
				map.put(key[k], "");
			}*/
		}

		return map;
	}
	/**
	 * Map타입의 데이터 암/복호화
	 * @param data 암/복호화 대상 map
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 map
	 */
	public static HashMap<String, Object> convertSeeding(HashMap<String, Object> map, String[] key, String seedMode ){
		for(int k=0; k<key.length; k++){
		    if(map.get(key[k]) != null){
    			String value = (String)map.get(key[k]);
    			//String temp=Aprocni_Seeding.seeding_always(value, "DEC");
    			//String temp = seeding_always(value, seedMode);
    			String temp = seeding_always_enc_check(value);
    			if (temp.equals("") && seedMode=="DEC") // 복호화시 공백이 나오면 평문 전달
    				temp = value;
    			else
    				temp = seeding_always(value, seedMode);

    			/*if(!value.equals("null")){
    				if(temp.equals("")){
    					map.put(key[k], seeding_always(value, seedMode));
    				}
    			}else if(value.equals("null")){
    				map.put(key[k], "");
    			}*/

    			map.put(key[k], temp);
		    }
		}

		return map;
	}

    public static Map<String, Object> convertSeeding(Map<String, Object> map, String[] key, String seedMode ){
        for(int k=0; k<key.length; k++){
            if(map.get(key[k]) != null){
                String value = (String)map.get(key[k]);
                //String temp=Aprocni_Seeding.seeding_always(value, "DEC");
                //String temp = seeding_always(value, seedMode);
                String temp = seeding_always_enc_check(value);
                if (temp.equals("") && seedMode=="DEC") // 복호화시 공백이 나오면 평문 전달
                    temp = value;
                else
                    temp = seeding_always(value, seedMode);

                /*if(!value.equals("null")){
                    if(temp.equals("")){
                        map.put(key[k], seeding_always(value, seedMode));
                    }
                }else if(value.equals("null")){
                    map.put(key[k], "");
                }*/

                map.put(key[k], temp);
            }
        }

        return map;
    }

	public static String convertSeeding(String[] str, String seedMode )  {
	  int length = str.length;
	  if (length == 0) return null;

	  StringBuilder connectBuilder = new StringBuilder(200);

	  for (int i = 0; i < length; i++) {

	    String value = str[i];

	    String temp = seeding_always_enc_check(value);
      if (temp.equals("") && seedMode=="DEC") // 복호화시 공백이 나오면 평문 전달
        temp = value;
      else
        temp = seeding_always(value, seedMode);

//      System.out.println("temp : " + temp);

      connectBuilder.append(temp);

      if (i != length - 1) {
        connectBuilder.append(",");
      }
	  }

	  return connectBuilder.toString();
	}

	/**
	 * ArrayList타입의 데이터 암/복호화
	 * @param data 암/복호화 대상 ArrayList
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 ArrayList
	 */
	public static ArrayList<HashMap<String, Object>> convertSeeding(ArrayList<HashMap<String, Object>> mapList, String[] key, String seedMode ){
		for(int i=0; i<mapList.size(); i++){
		  HashMap<String, Object> temp = mapList.get(i);
			temp = convertSeeding(temp, key, seedMode);
			mapList.set(i,temp);
		}

		return mapList;
	}

    /**
     * ArrayList타입의 데이터 암/복호화
     * @param data 암/복호화 대상 ArrayList
     * @param key 암/복호화할 대상키
     * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
     * @return 암호화 완료된 데이터 ArrayList
     */
    public static List<Map<String, Object>> convertSeeding(List<Map<String, Object>> mapList, String[] key, String seedMode ){
        for(int i=0; i<mapList.size(); i++){
            Map<String, Object> temp = (Map<String, Object>)mapList.get(i);
            temp = convertSeeding(temp, key, seedMode);
            mapList.set(i,temp);
        }

        return mapList;
    }


	/**
	 * Map타입의 데이터 암/복호화 // 리스트 * 처리 추가
	 * @param data 암/복호화 대상 map
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 map
	 */
	public static HashMap<String, Object> convertPrivateInfoData(HashMap<String, Object> map, String[] key, String seedMode,String setKey ){
		for(int k=0; k<key.length; k++){
			String value = (String)map.get(key[k]);

			map.put(key[k], getMethod(seedMode, value, setKey));
		}

		return map;
	}


	/**
	 * Map타입의 데이터 암/복호화 // 리스트 * 처리 추가
	 * @param data 암/복호화 대상 map
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 map
	 */
	public static Map<String, Object> convertPrivateInfoData(Map<String, Object> map, String[] key, String seedMode,String setKey ){
		for(int k=0; k<key.length; k++){
			String value = (String)map.get(key[k]);

			map.put(key[k], getMethod(seedMode, value, setKey));
		}

		return map;
	}


	/**
	 * ArrayList타입의 데이터 암/복호화 // 리스트 * 처리 추가
	 * @param data 암/복호화 대상 ArrayList
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 ArrayList
	 */
	public static ArrayList<HashMap<String, Object>> convertSeedingPrivateInfoData(ArrayList<HashMap<String, Object>> mapList, String[] key, String seedMode ,String setKey){
		for(int i=0; i<mapList.size(); i++){
		  HashMap<String, Object> temp = mapList.get(i);
			temp = convertPrivateInfoData(temp, key, seedMode,setKey);
			mapList.set(i,temp);
		}

		return mapList;
	}

	/**
	 * ArrayList타입의 데이터 암/복호화 // 리스트 * 처리 추가 ( MAP )
	 * @param data 암/복호화 대상 ArrayList
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 ArrayList
	 */
	public static ArrayList<Map<String, Object>> convertSeedingPrivateInfoDataMap(ArrayList<Map<String, Object>> mapList, String[] key, String seedMode ,String setKey){
		for(int i=0; i<mapList.size(); i++){
			Map<String, Object> temp = mapList.get(i);
			temp = convertPrivateInfoData(temp, key, seedMode,setKey);
			mapList.set(i,temp);
		}

		return mapList;
	}
	public static List<Map<String, Object>> convertSeedingPrivateInfoDataMap(List<Map<String, Object>> mapList, String[] key, String seedMode ,String setKey){
	   for(int i=0; i<mapList.size(); i++){
	      Map<String, Object> temp = mapList.get(i);
	      temp = convertPrivateInfoData(temp, key, seedMode,setKey);
	      mapList.set(i,temp);
	   }
	   
	   return mapList;
	}

	/**
	 * List타입의 데이터 암/복호화 // 리스트 * 처리 추가
	 * @param data 암/복호화 대상 ArrayList
	 * @param key 암/복호화할 대상키
	 * @param seedMode 암호화나 복호화 여부(암호화 : ENC, 복호화 : DEC)
	 * @return 암호화 완료된 데이터 ArrayList
	 */
	public static List<Map<String, Object>> convertSeedingPrivateInfoData(List<Map<String, Object>> mapList, String[] key, String seedMode ,String setKey){
		for(int i=0; i<mapList.size(); i++){
			Map<String, Object> temp = mapList.get(i);
			temp = convertPrivateInfoData(temp, key, seedMode,setKey);
			mapList.set(i,temp);
		}

		return mapList;
	}
	
	public static ArrayList<Map<String, Object>> convertSeedingPrivateInfoDataArray(ArrayList<Map<String, Object>> mapList, String[] key, String seedMode ,String setKey){
	   for(int i=0; i<mapList.size(); i++){
	      Map<String, Object> temp = mapList.get(i);
	      temp = convertPrivateInfoData(temp, key, seedMode,setKey);
	      mapList.set(i,temp);
	   }
	   
	   return mapList;
	}


	/**
	 * set메소드에 대해 지정된 값은 무조건 암호화 처리
	 * @param 처리문자열
	 * @return 암호화 완료된 데이터
	 */
	public static String setMethod(String checkValue ){
		//String temp = Seeding.seeding_always(checkValue, "DEC");
		String temp = Seeding.seeding_always_enc_check(checkValue);

		if(!temp.equals("")){
			temp = checkValue;
		}else{
			temp = Seeding.seeding_always(checkValue, "ENC");
		}
		return temp;
	}


	/**
	 * get메소드에 대해 지정된 값은 선택된 encType에 맞춰 암/복호화 처리
	 * @param 암/복호화 타입 (ENC:암호화, DEC:복호화)
	 * @param 처리문자열
	 * @return 암호화 완료된 데이터
	 */
	public static String getMethod(String encType, String checkValue ){
		return getMethod(encType, checkValue, null);
	}

	public static String getMethod(ArrayList<HashMap<String, Object>> list, String key, String kind, String encType) {
    for (HashMap<String, Object> hashMap : list) {
      hashMap.put(key, getMethod(encType, (String)hashMap.get(key), kind));
    }

    return "ok";
  }

	/**
	 * get메소드에 대해 지정된 값은 선택된 encType에 맞춰 암/복호화 처리
	 * @param 암/복호화 타입 (ENC:암호화, DEC:복호화)
	 * @param 처리문자열
	 * @param 출력된 문자열 ** 처리방식 (CARD, BANK, PHONE, NAME)
	 * @return 암호화 완료된 데이터
	 */
	public static String getMethod(String encType, String checkValue, String hiddenType ){
		String temp = "";
		if(encType.equals("ENC")){
			temp=checkValue;
		}else {
			temp=Seeding.seeding_always(checkValue, "DEC");
			if(hiddenType != null){
				temp = StringUtil.changePrivateInfoData(hiddenType, temp);
			}
		}

		return temp;
	}



	/**
	 * set메소드에 대해 지정된 값은 무조건 복호화 처리
	 * @param 처리문자열
	 * @return 암호화 완료된 데이터
	 */
	public static String setMethodDec(String checkValue ){
		//String temp = Seeding.seeding_always(checkValue, "DEC");
		String temp = Seeding.seeding_always_enc_check(checkValue);

		if(temp.equals("")){
			temp = checkValue;
		}else{

		}
		return temp;
	}

	/**
	 * 암호화된 데이터 인지 확인(데이터를 복호화후 암호화 한경우 동일하면 디코딩값 전달 아니면 공백 전달)
	 * @param data 암호화된 데이터
	 * @return param데이터가 암호화된경우만 디코딩 문자열 리턴 그외는 공백..
	 */
	public static String seeding_always_enc_check(String data) {
		String dec = seeding(data, "DEC", Aprocni_Key);
		String enc = seeding(dec, "ENC", Aprocni_Key);

		if(data == null || data.equals("")) return "";

		if(data.equals(enc)){ // 다시 암호화 했을때의 값이 동일하면 디코딩된 값 전달
			return dec;
		}else{
			return "";
		}
	}

	public static String convertSeeding(String str, String seedMode )  {
    if (str == null || str.equals("") || str.trim().equals("")) return null;
  
    String value = str;

    String temp = seeding_always_enc_check(value);
    if (temp.equals("") && seedMode=="DEC") // 복호화시 공백이 나오면 평문 전달
      temp = value;
    else
      temp = seeding_always(value, seedMode);

    str = temp;

    return str;
  }

}
