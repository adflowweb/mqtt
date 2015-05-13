/*
 * 
 */
package kr.co.adflow.pms.domain.validator;

import kr.co.adflow.pms.core.config.StaticConfig;

import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class UserValidator.
 */
@Component
public class UserValidator {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		UserValidator valid = new UserValidator();

//		System.out.println("01012341234::"
//				+ valid.getRequestType("01012341234"));
//		System.out.println("82*1234*1234::"
//				+ valid.getRequestType("82*1234*1234"));
//		System.out.println("01*1234*1234::"
//				+ valid.getRequestType("01*1234*1234"));
//		System.out.println("82*12*1234::" + valid.getRequestType("82*12*1234"));
//		System.out.println("01*12*1234::" + valid.getRequestType("01*12*1234"));
//		System.out.println("01123412a34::"
//				+ valid.getRequestType("01123412a34"));
//		System.out.println("82*200*8003::"
//				+ valid.getRequestType("82*200*8003"));
//
//		System.out.println("01012341234::"
//				+ valid.validRequestValue("01012341234"));
//		System.out.println("82*1234*1234::"
//				+ valid.validRequestValue("82*1234*1234"));
//		System.out.println("01*1234*1234::"
//				+ valid.validRequestValue("01*1234*1234"));
//		System.out.println("82*12*1234::"
//				+ valid.validRequestValue("82*12*1234"));
//		System.out.println("01*12*1234::"
//				+ valid.validRequestValue("01*12*1234"));
//		System.out.println("01123412a34::"
//				+ valid.validRequestValue("01123412a34"));
//		System.out.println("82*200*8003::"
//				+ valid.validRequestValue("82*200*8003"));
//
//		System.out.println("01012341234::"
//				+ valid.getRegstPhoneNo("01012341234"));
//		System.out.println("82*1234*1234::"
//				+ valid.getRegstUfmiNo("82*1234*1234"));
//		System.out.println("01*1234*1234::"
//				+ valid.getRegstUfmiNo("01*1234*1234"));
//		System.out.println("82*1234*1234::"
//				+ valid.getRegstUfmiNo("82*12*1234"));
//		System.out.println("01*1234*1234::"
//				+ valid.getRegstUfmiNo("01*12*1234"));
//		System.out.println("82*200*8003::"
//				+ valid.getRegstUfmiNo("82*200*8003"));
//
//		System.out.println("01012341234::"
//				+ valid.getSubscribPhoneNo("01012341234"));
//		System.out.println("82*1234*1234::"
//				+ valid.getSubscribUfmi1("82*1234*1234"));
//		System.out.println("01*1234*1234::"
//				+ valid.getSubscribUfmi2("01*1234*1234"));
//		System.out.println("82*1234*1234::"
//				+ valid.getSubscribUfmi1("82*12*1234"));
//		System.out.println("01*1234*1234::"
//				+ valid.getSubscribUfmi2("01*12*1234"));
//		System.out.println("82*200*8003::"
//				+ valid.getSubscribUfmi1("82*200*8003"));

	}

	/**
	 * Gets the request type.
	 *
	 * @param requestVal the request val
	 * @return the request type
	 */
	public int getRequestType(String requestVal) {
		int resultType = 0;

		if (!this.checkRequestValue(requestVal)) {
			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_ERROR;
		}

		if (requestVal.substring(0, 3).equals("010")) {
			//010 전화번호도 허용안하기로 함.
//			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_PHONE;
			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_ERROR;
		}

		//kicho(20150410) -P1 규칙 : 82 * 1~6자리 * 1 ~6자리, P2 규칙: 00 ~ 41 * 1~4자리 * 1~4자리 - [01-start]		
//		if (requestVal.substring(0, 2).equals("82")
//				&& requestVal.substring(2, 3).equals("*")
//				&& requestVal.substring(requestVal.length() - 5,
//						requestVal.length() - 4).equals("*")) {
//			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI1;
//		}
//		
//		if (!requestVal.substring(0, 2).equals("82")
//				&& requestVal.substring(2, 3).equals("*")
//				&& requestVal.substring(requestVal.length() - 5,
//						requestVal.length() - 4).equals("*")) {
//			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI2;
//		}
		//kicho(20150410) -P1 규칙 : 82 * 1~6자리 * 1 ~6자리, P2 규칙: 00 ~ 41 * 1~4자리 * 1~4자리 - [01-end]
		
		//kicho(20150410) -P1 규칙 : 82 * 1~6자리 * 1 ~6자리, P2 규칙: 00 ~ 41 * 1~4자리 * 1~4자리 - [02-start]
		int firstT = requestVal.indexOf('*');
		int lastT = requestVal.lastIndexOf('*');
		int lengT = requestVal.length();
		
//		System.out.println("firstT :"+ firstT + ", lastT :"+lastT + ", len :"+ lengT);
		//P1 check		
		if(requestVal.substring(0, 2).equals("82") 
				&& firstT + 1 < lastT  
				&& lastT - firstT < 8  
				&& lastT+1 < lengT 
				&& lengT - lastT < 8) {
			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI1;
		}
		
		//P2 check		
		if(!requestVal.substring(0, 2).equals("82") 
				&& firstT + 1 < lastT  
				&& lastT - firstT < 6  
				&& lastT+1 < lengT 
				&& lengT - lastT < 6) {
			return StaticConfig.SERVICE_REQUEST_FORMAT_TYPE_UFMI2;
		}
		
		
		
		//kicho(20150410) -P1 규칙 : 82 * 1~6자리 * 1 ~6자리, P2 규칙: 00 ~ 41 * 1~4자리 * 1~4자리 - [02-end]

		return resultType;
	}

	/**
	 * Valid request value.
	 *
	 * @param val the val
	 * @return true, if successful
	 */
	public boolean validRequestValue(String val) {

		int result = this.getRequestType(val);

		if (result > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Gets the regst phone no.
	 *
	 * @param val the val
	 * @return the regst phone no
	 */
	public String getRegstPhoneNo(String val) {

		return "+82" + val.substring(1);
	}

	/**
	 * Gets the subscrib phone no.
	 *
	 * @param val the val
	 * @return the subscrib phone no
	 */
	public String getSubscribPhoneNo(String val) {

		return "mms/82" + val.substring(1);
	}

	/**
	 * Gets the regst ufmi no.
	 *
	 * @param val the val
	 * @return the regst ufmi no
	 */
	public String getRegstUfmiNo(String val) {

		return val;
	}

	/**
	 * Gets the subscrib ufmi1.
	 *
	 * @param val the val
	 * @return the subscrib ufmi1
	 */
	public String getSubscribUfmi1(String val) {

		return "mms/P1/" + this.repStar(val);
	}

	/**
	 * Gets the subscrib ufmi2.
	 *
	 * @param val the val
	 * @return the subscrib ufmi2
	 */
	public String getSubscribUfmi2(String val) {

		return "mms/P2/" + this.repStar(val);
	}

	/**
	 * Rep star.
	 *
	 * @param val the val
	 * @return the string
	 */
	private String repStar(String val) {

		String[] temp = null;
		String result = null;

		temp = val.split("\\*");

		// System.out.println("temp len"+ temp.length);

		result = temp[0] + "/" + temp[1] + "/p" + temp[2];

		return result;
	}

	/**
	 * Check request value.
	 *
	 * @param val the val
	 * @return true, if successful
	 */
	private boolean checkRequestValue(String val) {

		//kicho(20150413) -Receiver length 6 ~ 16 chage 
		if (val == null || val.trim().length() < 6 || val.trim().length() > 16) {
			return false;
		} else {
			return true;
		}

	}

}
