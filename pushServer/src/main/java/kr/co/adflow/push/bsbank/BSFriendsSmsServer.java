package kr.co.adflow.push.bsbank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BSFriendsSmsServer {

	// private static final String TEST_IP = "130.80.101.49"; // TEST SERVER IP
	// private static final String REAL_IP = "130.80.101.48"; // REAL SERVER IP

	private static final boolean DEBUG = true;

	private static String SERVER_IP = "";

	private static final int PORT = 3000;

	private static final String ENCODING_TYPE = "EUC-KR";

	// PUBLIC DATA
	private static final String P_USER_ID = "friends"; // 아이디
	private static final String P_USER_PWD = "bsfriends1!"; // (BODY)패스워드

	// BIND_DATA
	private static final String B_MSGLEN = "68"; // (HEADER)body 64byte + 4byte
	private static final String B_MSGTYPE = "0001"; // (HEADER)BIND MSGTYPE
	private static final String B_TYPE = "S"; // (BODY)송,수신 타입 S : 송신 , R : 수신
	private static final String B_VERSION = ""; // (BODY)버전명

	// ALIVE CHECK
	private static final String A_MSGLEN = "24";
	private static final String A_MSGTYPE = "0009";
	private static final String A_SN = "";

	// ACK PARAM
	private static final String BIND_ACK = "1001";
	private static final String RESULT_SUCCESS = "0000";
	private static final int BIND_ACK_LENGTH = 66;
	private static final int DELIVERY_SMS_LENGTH = 347;
	private static final int ALIVE_ACK_LENGTH = 38;

	private static OutputStream os = null;
	private static BufferedOutputStream bos = null;

	private static BindServer bindThread;
	private static Timer mTimer;
	private static BufferedInputStream bis;

	static ServerSocket server = null;

	public static void startServer(String serverIp) {
		// BSFriendsWindow.setLogText("Start");

		SERVER_IP = serverIp;

		Socket socket = null;
		try {
			// SMS 서버 Bind Thread
			bindThread = new BindServer(socket);
			bindThread.start();

			// 로컬 소켓 생성
			server = new ServerSocket(PORT);
			socket = server.accept();
			// BSFriendsWindow.setLogText("CONNECTED ADDR : "
			// + socket.getInetAddress().getHostAddress());

			// 로컬 리시버
			ReceiveServer thread = new ReceiveServer(socket);
			thread.start();
		} catch (IOException ie) {
			// BSFriendsWindow.setLogText("Start Fail");
			// BSFriendsWindow.setLogText(ie.getMessage());
		}
	}

	public static void stopServer() {
		// BSFriendsWindow.setLogText("Stop");
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class DeliverySMS extends Thread {
		private String msg;

		public DeliverySMS(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			// 내용이 있으면 메시지로
			bindThread.sendMessage(msg);
		}
	}

	/**
	 * 수신처리 쓰레드
	 * 
	 * @author OU10398
	 * 
	 */
	private static class ReceiveServer extends Thread {
		private Socket socket;

		public ReceiveServer(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			InputStream os = null;
			while (true) {
				BufferedInputStream bis = null;
				try {
					os = socket.getInputStream();
					bis = new BufferedInputStream(os);

					byte[] bindAck = new byte[357];
					bis.read(bindAck);

					String msg = new String(bindAck);

					// BSFriendsWindow.setLogText("RECEIVE MSG : " +
					// msg.trim());

					if (false == msg.trim().equals("")) {
						// 내용이 있으면 메시지로
						new DeliverySMS(msg).start();
					} else {
						// 내용이 없으면 소켓 강제로 닫고 다시 시작..
						// BSFriendsWindow.setLogText("Client disconnected");

						try {
							socket.close();
							socket = null;

							server.close();
							server = null;

							// BSFriendsWindow.setLogText("ReStart");
							server = new ServerSocket(PORT);
							socket = server.accept();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException ie) {
					// BSFriendsWindow.setLogText(ie.getMessage());
					// BSFriendsWindow.setLogText("Client disconnected");

					try {
						socket.close();
						socket = null;

						server.close();
						server = null;

						// BSFriendsWindow.setLogText("ReStart");
						server = new ServerSocket(PORT);
						socket = server.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 바인드를 수행하는 쓰레드
	 * 
	 * @author OU10398
	 * 
	 */
	private static class BindServer extends Thread {
		private Socket socket;

		public BindServer(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				socket = new Socket(SERVER_IP, PORT);
				if (DEBUG) {
					// BSFriendsWindow.setLogText("Server Connection Success");
				}

				try {
					os = socket.getOutputStream();
					bos = new BufferedOutputStream(os);

					bis = new BufferedInputStream(socket.getInputStream());

					setBindServerData(bos);
					bos.flush();

					byte[] bindAck = new byte[BIND_ACK_LENGTH];
					bis.read(bindAck);

					String bindAckString = new String(bindAck);

					if (bindAckString.length() >= BIND_ACK_LENGTH) {
						if (DEBUG) {
							// BSFriendsWindow.setLogText("BIND ACK  :"
							// + bindAckString);
							// BSFriendsWindow.setLogText("Length    :"
							// + bindAckString.length());
						}

						String msgtype = "";
						msgtype = bindAckString.subSequence(10, 14).toString();

						if (BIND_ACK.equals(msgtype)) {
							String result = bindAckString.subSequence(14, 18)
									.toString();
							if (RESULT_SUCCESS.equals(result)) {
								// BIND_ACK SUCCESS
								if (DEBUG) {
									getBindAllParam(bindAckString);
								}

								// BSFriendsWindow.setLogText("BIND SUCCESS");
								System.out.println("BIND SUCCESS");

								// ALIVE CHECK
								new AliveCheck().start();
							} else {
								if (DEBUG) {
									// BSFriendsWindow
									// .setLogText("바인드 실패로 인한 종료 ERROR CODE : "
									// + getBindError(result));
								}

								// BSFriendsWindow.setLogText("BIND FAIL");
								try {
									// BSFriendsWindow
									// .setLogText("BIND retry...wait 5 seconds");
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								// BSFriendsWindow.setLogText("BIND RETRY");
								// SMS 서버 Bind Thread
								bindThread = new BindServer(socket);
								bindThread.start();
							}
						} else {
							if (DEBUG) {
								// BSFriendsWindow
								// .setLogText("바인드 ACK가 아님 소켓종료 : "
								// + msgtype);
							}
							socket.close();
							os.close();
							bos.close();
							bis.close();
						}
					}
				} catch (IOException e) {
					if (DEBUG)
						// BSFriendsWindow.setLogText("바인드 요청 실패 소켓종료 : " + e);
						socket.close();
					os.close();
					bos.close();
					bis.close();
				}
			} catch (IOException ie) {
				if (DEBUG) {
					// BSFriendsWindow.setLogText("소켓생성 실패 : " +
					// ie.getMessage());
				}

			}
		}

		private boolean sendMessage(String data) {
			// DELIVER SMS
			try {
				byte[] DATA = new byte[357];

				setData(DATA, data);
				bos.write(DATA);
				bos.flush();

				BufferedInputStream bis = new BufferedInputStream(
						socket.getInputStream());
				byte[] deliveryAck = new byte[DELIVERY_SMS_LENGTH];
				bis.read(deliveryAck);
				String deliveryAckString = new String(deliveryAck);
				if (DEBUG) {
					// BSFriendsWindow.setLogText("DELI ACK  :"
					// + deliveryAckString);
					// BSFriendsWindow.setLogText("Length    :"
					// + deliveryAckString.length());
				}

				if (DEBUG) {
					getDeliveryAllParam(deliveryAckString);
				}

				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		}

	}

	private static class AliveCheck extends Thread {
		public AliveCheck() {
		}

		@Override
		public void run() {
			// ALIVE ACK
			mTimer = new Timer();
			mTimer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					try {
						setAliveServerData(bos);
						bos.flush();

						byte[] aliveAck = new byte[ALIVE_ACK_LENGTH];
						bis.read(aliveAck);
						String aliveAckString = new String(aliveAck);
						if (DEBUG) {
							getAliveAllParam(aliveAckString);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 0, 300000);
		}
	}

	/**
	 * Alive 시 데이터 채우기
	 * 
	 * @param bos
	 * @throws IOException
	 */
	private static void setAliveServerData(BufferedOutputStream bos)
			throws IOException {
		// HEADER
		byte[] MSGLEN = new byte[10]; // Body의 길이 + 4 Byte
		byte[] MSGTYPE = new byte[4];

		// BODY - BIND
		byte[] SN = new byte[20]; // SN

		setData(MSGLEN, A_MSGLEN);
		bos.write(MSGLEN);

		setData(MSGTYPE, A_MSGTYPE);
		bos.write(MSGTYPE);

		setData(SN, A_SN);
		bos.write(SN);
	}

	/**
	 * 바인드 시 데이터 채우기
	 * 
	 * @param bos
	 * @throws IOException
	 */
	private static void setBindServerData(BufferedOutputStream bos)
			throws IOException {
		// HEADER
		byte[] MSGLEN = new byte[10]; // Body의 길이 + 4 Byte
		byte[] MSGTYPE = new byte[4];

		// BODY - BIND
		byte[] BIND_ID = new byte[16]; // 요청자 ID
		byte[] BIND_PWD = new byte[16]; // 요청자 암호
		byte[] LINE_TYPE = new byte[1]; // S : 송신 , R : 수신
		byte[] VERSION = new byte[31]; // 접속규약 Version 정보

		setData(MSGLEN, B_MSGLEN);
		bos.write(MSGLEN);

		setData(MSGTYPE, B_MSGTYPE);
		bos.write(MSGTYPE);

		setData(BIND_ID, P_USER_ID); // 아이디
		bos.write(BIND_ID);

		setData(BIND_PWD, P_USER_PWD); // 패스워드
		bos.write(BIND_PWD);

		setData(LINE_TYPE, B_TYPE);
		bos.write(LINE_TYPE);

		setData(VERSION, B_VERSION);
		bos.write(VERSION);
	}

	/**
	 * 자리수에 대해 입력한 만큼 데이터를 채우고 나머지는 공백을 채운다.
	 * 
	 * @param arryByte
	 * @param str
	 * @throws UnsupportedEncodingException
	 */
	private static void setData(byte[] arryByte, String str)
			throws UnsupportedEncodingException {

		// 문자열 값이 널이면 전부 공백으로 채우기 위해
		if (str == null) {
			str = "";
		}

		// 필드에 채울 문자열을 바이트배열로 변환
		byte[] bytes = str.getBytes(ENCODING_TYPE);

		// 실제데이터 값의 바이트배열길이
		int endIdx = 0;

		// 실제데이터 바이트배열 설정
		if (arryByte.length >= bytes.length) {
			endIdx = bytes.length; // 필드의 배열이 실제데이터배열보다 크거나 같을 경우
		} else {
			endIdx = arryByte.length; // 필드의 배열이 실제데이터배열보다 작을 경우
		}

		// 필드배열에 실제데이터배열값으로 채운다.
		for (int i = 0; i < endIdx; i++) {
			arryByte[i] = bytes[i];
		}
		// 실제데이터값이 채워지지 않은 배열은 공백으로 채운다.
		for (int j = endIdx; j < arryByte.length; j++) {
			arryByte[j] = 0x00;
		}
	}

	@SuppressWarnings("unused")
	private static String getData(byte[] arryByte)
			throws UnsupportedEncodingException {

		if (arryByte == null) {
			return "";
		}

		return new String(arryByte, ENCODING_TYPE);
	}

	/**
	 * ALIVE_ACK 로그
	 * 
	 * @param result
	 */
	private static void getAliveAllParam(String result) {
		int[] addoffset = { 10, 4, 4, 20 };
		int offset = 0;
		// BSFriendsWindow
		// .setLogText("-------------------------------------------ALIVE_ACK");
		// BSFriendsWindow.setLogText("MSGLEN    :"
		// + result.subSequence(offset, offset += addoffset[0]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("MSGTYPE   :"
		// + result.subSequence(offset, offset += addoffset[1]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("RESULT    :"
		// + result.subSequence(offset, offset += addoffset[2]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("SN        :"
		// + result.subSequence(offset, offset += addoffset[3]).toString()
		// + "|");
		// BSFriendsWindow
		// .setLogText("----------------------------------------------------");
	}

	/**
	 * BIND_ACK 로그
	 * 
	 * @param result
	 */
	private static void getBindAllParam(String result) {
		int[] addoffset = { 10, 4, 4, 16, 32 };
		int offset = 0;
		// BSFriendsWindow
		// .setLogText("--------------------------------------------BIND_ACK");
		// BSFriendsWindow.setLogText("MSGLEN    :"
		// + result.subSequence(offset, offset += addoffset[0]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("MSGTYPE   :"
		// + result.subSequence(offset, offset += addoffset[1]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("RESULT    :"
		// + result.subSequence(offset, offset += addoffset[2]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("PREFIX    :"
		// + result.subSequence(offset, offset += addoffset[3]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("SEED_KEY  :"
		// + result.subSequence(offset, offset += addoffset[4]).toString()
		// + "|");
		// BSFriendsWindow
		// .setLogText("----------------------------------------------------");
	}

	/**
	 * DELIVERY_ACK 로그
	 * 
	 * @param result
	 */
	private static void getDeliveryAllParam(String result) {
		int[] addoffset = { 10, 4, 4, 20, 20, 6, 10, 10, 10, 20, 40, 16, 16 };
		int offset = 0;
		// BSFriendsWindow
		// .setLogText("-------------------------------------DELIVER_SMS_ACK");
		// BSFriendsWindow.setLogText("MSGLEN    :"
		// + result.subSequence(offset, offset += addoffset[0]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("MSGTYPE   :"
		// + result.subSequence(offset, offset += addoffset[1]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("RESULT    :"
		// + result.subSequence(offset, offset += addoffset[2]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("SN        :"
		// + result.subSequence(offset, offset += addoffset[3]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("USER_ID   :"
		// + result.subSequence(offset, offset += addoffset[4]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("COMP_CD   :"
		// + result.subSequence(offset, offset += addoffset[5]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("PLACE_CD  :"
		// + result.subSequence(offset, offset += addoffset[6]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("BIZ_ID1   :"
		// + result.subSequence(offset, offset += addoffset[7]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("BIZ_ID2   :"
		// + result.subSequence(offset, offset += addoffset[8]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("CUST_ID   :"
		// + result.subSequence(offset, offset += addoffset[9]).toString()
		// + "|");
		// BSFriendsWindow.setLogText("COMP_KEY  :"
		// + result.subSequence(offset, offset += addoffset[10])
		// .toString() + "|");
		// BSFriendsWindow.setLogText("DESTADDR  :"
		// + result.subSequence(offset, offset += addoffset[11])
		// .toString() + "|");
		// BSFriendsWindow.setLogText("CALLBACK  :"
		// + result.subSequence(offset, offset += addoffset[12])
		// .toString() + "|");
		// BSFriendsWindow
		// .setLogText("----------------------------------------------------");
	}

	private static String getBindError(String result) {
		if (isNumber(result)) {
			int errorCd = Integer.parseInt(result);
			switch (errorCd) {
			case 0: // 0000 -
				return "E_OK";

			case 1: // 0001 - 시스템 장애
				return "E_SYSFAIL";

			case 2: // 0002 - 인증실패, 직후 연결을 끊음
				return "E_AUTH_FAIL";

			case 4: // 0004 - BIND 안됨
				return "E_NOT_BOUND";

			case 20: // 0020 - UNKNOWN
				return "E_UNKNOWN";

			default:
				return "UNKNOWN ERROR";
			}
		} else {
			return "NOT ERROR CODE";
		}
	}

	public static String getSMSDeliveryError(String result) {
		if (isNumber(result)) {
			int errorCd = Integer.parseInt(result);
			switch (errorCd) {
			case 0: // 0000 -
				return "E_OK";

			case 1: // 0001 - 시스템 장애
				return "E_SYSFAIL";

			case 3: // 0003 - 메시지 형식 오류
				return "E_FORMAT_ERR";

			case 13: // 0013 - 계열사코드 없음
				return "E_COMP_ERR";

			case 14: // 0014 - 부서코드 없음
				return "E_PLACE_ERR";

			case 15: // 0015 - 업무구분코드1 없음
				return "E_BIZ_ID1_ERR";

			case 16: // 0016 - 업무구분코드2 없음
				return "E_BIZ_ID2_ERR";

			case 17: // 0017 - CallbackURL 사용자 아님
				return "E_NO_URLUSER";

			case 18: // 0018 - 메시지 중복 발송
				return "E_DUP_MSG";

			case 19: // 0019 - 월 송신 건수 초과
				return "E_FLOWCONTROL";

			case 20: // 0020 - UNKNOWN
				return "E_UNKNOWN";

			case 22: // 0022 - 착신번호 에러(없는 국번)
				return "E_DEST_CODE";

			case 26: // 0026 - LG U+ 스팸 필터링
				return "E_DACOM_SPAM";

			default:
				return "UNKNOWN ERROR";
			}
		} else {
			return "NOT ERROR CODE";
		}
	}

	/**
	 * 수신 시 에러 처리 코드
	 * 
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getError(String result) {
		if (isNumber(result)) {
			int errorCd = Integer.parseInt(result);
			switch (errorCd) {
			case 0: // 0000
				return "E_OK";

			case 1: // 0001 - 시스템 장애
				return "E_SYSFAIL";

			case 5: // 0005 - 착신가입자 없음(미등록) (현재 사용안함)
				return "E_NO_DESTIN";

			case 6: // 0006 - 전송 성공
				return "E_SENT";

			case 7: // 0007 - 비가입자,결번,서비스정지
				return "E_INVALIDDST";

			case 8: // 0008 - 단말기 Power-off 상태
				return "E_POWEROFF";

			case 9: // 0009 - 음영
				return "E_HIDDEN";

			case 10: // 0010 - 단말기 메시지 FULL
				return "E_TERMFULL";

			case 11: // 0011 - 타임아웃
				return "E_TIMEOUT";

			case 13: // 0013 - 번호이동
				return "E_PORTED_OUT";

			case 14: // 0014 - 무선망에러
				return "E_ETC";

			case 20: // 0020 - 기타에러
				return "E_UNKNOWN";

			case 21: // 0021 - 착신번호 에러(자리수에러)
				return "E_DEST_SIZE";

			case 22: // 0022 - 착신번호 에러(없는 국번)
				return "E_DEST_CODE";

			case 23: // 0023 - 수신거부 메시지 없음
				return "E_MSG_FMT";

			case 24: // 0024 - 21시 이후 광고
				return "E_ADV21";

			case 25: // 0025 - 성인광고, 대출광고 등 기타 제한
				return "E_ADV";

			case 27: // 0027 - 야간발송차단
				return "E_NIGHTBLOCK";

			case 40: // 0040 - 단말기착신거부(스팸등)
				return "E_NO_ALLOW";

			case 70: // 0070 - 기타오류 - KTF URL
				return "E_ETC_URL";

			case 80: // 0080 - 결번(이통사 Nack) - SKT URL
				return "E_INVALD_USER";

			case 81: // 0081 - 전송실패(정지고객등) - SKT URL
				return "E_STOP_USER";

			case 82: // 0082 - 번호이동 DB 조회불가 - SKT URL
				return "E_MOVE_DB";

			case 83: // 0083 - 번호이동번호 - SKT URL
				return "E_MOVE_SKT";

			case 84: // 0084 - 타임아웃(이통사) - SKT URL
				return "E_TIMEOUT_SKT";

			case 85: // 0085 - 전송실패(기타에러) - SKT URL
				return "E_TIMEOUT_ETC";

			default:
				return "UNKNOWN ERROR";
			}
		} else {
			return "NOT ERROR CODE";
		}
	}

	/**
	 * 번호 유효성 체크
	 * 
	 * @param number
	 * @return
	 */
	private static boolean isNumber(String number) {
		if (number == null || "".equals(number)) {
			return false;
		}

		Pattern p = Pattern.compile("([\\p{Digit}]+)(([.]?)([\\p{Digit}]+))?");
		Matcher m = p.matcher(number);
		return m.matches();
	}
}
