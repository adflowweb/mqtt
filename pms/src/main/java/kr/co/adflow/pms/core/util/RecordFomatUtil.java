package kr.co.adflow.pms.core.util;

public class RecordFomatUtil {
	public static String intFormat(int valueInt, int stringLen) {
		String valueStr;
		int valueLen;
		
		
		StringBuffer sb = new StringBuffer();
		
		try {
			valueStr = Integer.toString(valueInt);
			valueLen = valueStr.length();
			
			// length check
			if (stringLen < valueLen) {
				new RuntimeException("String Length is smail-StringLen:"+stringLen+", valueInt:"+valueInt);
			}
			
			//"0" add
			for (int i = 0; i < stringLen - valueLen; i++) {
				sb.append("0");
			}
			sb.append(valueStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String stingFormat(String value, int stringLen) {
		int valueLen;
		StringBuffer sb = new StringBuffer();
		
		try {
			valueLen = value.length();
			
			// length check
			if (stringLen < valueLen) {
				new RuntimeException("String Length is smail-StringLen:"+stringLen+", value:"+value);
			}
			
			sb.append(value);
			
			//space add
			for (int i = 0; i < stringLen - valueLen; i++) {
				sb.append(" ");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	public static String topicToUfmi(String topic) {
		
		String topicTemp = "";
		try {
			topicTemp = topic.substring(7, topic.length());
			topicTemp = topicTemp.replace("p", "");
			topicTemp = topicTemp.replace("/", "*");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return topicTemp;
	}
	

	public static String topicToUfmiNo(String topic) {
		int valueLen;
		StringBuffer sb = new StringBuffer();
		
		String pTalkVer = "";
		String topicTemp;
		try {
			pTalkVer = topic.substring(5, 6);
			topicTemp = topic.substring(7, topic.length());
			topicTemp = topicTemp.replace("p", "");
			
			int firstT = topicTemp.indexOf("/");
			int lastT = topicTemp.lastIndexOf("/");

			
			if(pTalkVer.equals("1")){
				//P-tail 1.0
				//82
				sb.append(topicTemp.substring(0,firstT));
				//국번
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(firstT,lastT).replace("/", "")),4));
				//개별번호
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(lastT,topicTemp.length()).replace("/", "")),5));
			} else if(pTalkVer.equals("2")){
				//P-tail 2.0
				//01~04
				sb.append(topicTemp.substring(0,firstT));
				//국번
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(firstT,lastT).replace("/", "")),4));
				//개별번호
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(lastT,topicTemp.length()).replace("/", "")),4));
			} else {
				new RuntimeException("ufmi Topic not valid - Topic:"+topic);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String ufmiNo(String topic) {
		int valueLen;
		StringBuffer sb = new StringBuffer();
		
		String pTalkVer = "";
		String topicTemp;
		try {
			pTalkVer = topic.substring(0, 2);
			topicTemp = topic;
			topicTemp = topicTemp.replace("p", "");
			
			int firstT = topicTemp.indexOf("*");
			int lastT = topicTemp.lastIndexOf("*");

		
			if(pTalkVer.equals("82")){
				//P-tail 1.0
				//82
				sb.append(topicTemp.substring(0,firstT));
				//국번
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(firstT,lastT).replace("*", "")),4));
				//개별번호
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(lastT,topicTemp.length()).replace("*", "")),5));
			} else if(pTalkVer.equals("2")){
				//P-tail 2.0
				//01~04
				sb.append(topicTemp.substring(0,firstT));
				//국번
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(firstT,lastT).replace("*", "")),4));
				//개별번호
				sb.append(RecordFomatUtil.intFormat(Integer.parseInt(topicTemp.substring(lastT,topicTemp.length()).replace("*", "")),4));
			} else {
				new RuntimeException("ufmi Topic not valid - Topic:"+topic);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
