package kr.co.adflow.push.busanbank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.adflow.push.dao.impl.MessageDaoImpl;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 6. 23.
 */
@Component
public class SMSDaoImpl implements SMSDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SMSDaoImpl.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(MessageDaoImpl.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("properties=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// PUBLIC DATA
	private static final String P_USER_ID = "friends"; // 아이디
	private static final String P_USER_PWD = "bsfriends1!"; // (BODY)패스워드

	// ALIVE CHECK
	private static final String A_MSGLEN = "24";
	private static final String A_MSGTYPE = "0009";
	private static final String A_SN = "";

	// BIND_DATA
	private static final String B_MSGLEN = "68"; // (HEADER)body 64byte + 4byte
	private static final String B_MSGTYPE = "0001"; // (HEADER)BIND MSGTYPE
	private static final String B_TYPE = "S"; // (BODY)송,수신 타입 S : 송신 , R : 수신
	private static final String B_VERSION = ""; // (BODY)버전명

	// ACK PARAM
	private static final String BIND_ACK = "1001";
	private static final String RESULT_SUCCESS = "0000";
	private static final int BIND_ACK_LENGTH = 66;
	private static final int DELIVERY_SMS_LENGTH = 347;
	private static final int ALIVE_ACK_LENGTH = 38;

	private static final String ENCODING_TYPE = "EUC-KR";
	private static final int RECEIVERPORT = 3000;
	private static final String SERVER_IP = "127.0.0.1";
	private static final int PORT = 4000;

	private int sendChannelInterval = Integer.parseInt(prop
			.getProperty("sendChannel.process.interval"));
	private int recvChannelInterval = Integer.parseInt(prop
			.getProperty("recvChannel.process.interval"));

	private ScheduledExecutorService sendChannel;
	private ScheduledExecutorService recvChannel;

	private ServerSocket recvServer;
	private Receiver receiver;
	private Sender sender;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		logger.info("SMSDAOImpl초기화시작()");
		sendChannel = Executors.newScheduledThreadPool(1);
		sendChannel.scheduleWithFixedDelay(new SendChannelHandler(), 0,
				sendChannelInterval, TimeUnit.SECONDS);
		logger.info("sendChannel핸들러가시작되었습니다.");
		recvChannel = Executors.newScheduledThreadPool(1);
		recvChannel.scheduleWithFixedDelay(new RecvChannelHandler(), 0,
				recvChannelInterval, TimeUnit.SECONDS);
		logger.info("recvChannel핸들러가시작되었습니다.");
		// initRecvServer();
		logger.info("ReceiveServer가시작되었습니다.");
		logger.info("SMSDAOImpl초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		try {
			logger.info("cleanUp시작()");
			sendChannel.shutdown();
			logger.info("sendChannel핸들러가종료되었습니다.");
			recvChannel.shutdown();
			logger.info("recvChannel핸들러가종료되었습니다.");
			if (recvServer != null && !recvServer.isClosed()) {
				recvServer.close();
				logger.info("리시브서버가종료되었습니다.");
			}
			logger.info("cleanUp종료()");
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
	}

	/*
	 * sms message 전송
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.SMSDAO#post()
	 */
	@Override
	public void post(String msg) throws Exception {
		sender.sendMsg(msg);
	}

	/**
	 * @author nadir93
	 * @date 2014. 6. 24.
	 */
	class SendChannelHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("sendChannel처리시작()");
			logger.debug("sender=" + sender);
			try {
				if (sender == null) {
					initSender();
				}
				sender.sendPING();
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
			logger.debug("sendChannel처리종료()");
		}
	}

	/**
	 * socket blocking
	 * 
	 * @throws Exception
	 */
	private void initSender() throws Exception {
		logger.debug("initSender시작()");
		if (sender != null) {
			logger.debug("sender가널값이아닙니다.");
			return;
		}
		logger.debug("sendSocket연결시작 서버주소=" + SERVER_IP + ", 포트=" + PORT);
		logger.debug("소케연결기다림..(블락킹)");
		Socket socket = new Socket(SERVER_IP, PORT);
		logger.debug("소켓이연결되었습니다.socket=" + socket);
		sender = new Sender(socket);
		sender.connect();
		// sender.start();
		logger.debug("샌더가시작되었습니다. sender=" + sender);
		logger.debug("initSender종료()");
	}

	/**
	 * @author nadir93
	 * @date 2014. 6. 24.
	 */
	class Sender {

		private Socket socket;
		private BufferedInputStream bis;
		private BufferedOutputStream bos;
		private boolean available = false;

		public Sender(Socket socket) throws Exception {
			logger.debug("Sender생성자시작(socket=" + socket + ")");
			this.socket = socket;
			bis = new BufferedInputStream(socket.getInputStream());
			bos = new BufferedOutputStream(socket.getOutputStream());

			// 기간계 시스템은 tcp/ip connect 성공즉시 bind 요청을 수행하여야 한다.
			// bind 요청은 최초 connect 접속시 한번만 수행하면 된다.
			setBindServerData(bos);
			bos.flush();
			logger.debug("bind메시지가전송되었습니다.");
			logger.debug("Sender생성자종료(this=" + this + ")");
		}

		public synchronized void sendPING() throws Exception {
			logger.debug("sendPING시작(샌더)");
			try {
				// healthCheck
				logger.debug("socket=" + socket);
				if (socket.isConnected()) {
					setAliveServerData(bos);
					bos.flush();
					logger.debug("AliveCheck메시지를전송하였습니다.");
					byte[] aliveAck = new byte[ALIVE_ACK_LENGTH];
					bis.read(aliveAck);
					String aliveAckString = new String(aliveAck);
					logger.debug("에크메시지=" + aliveAckString);
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
				cleanup();
			}
			logger.debug("sendPING종료(샌더)");
		}

		/**
		 * @param msg
		 * @throws Exception
		 */
		public synchronized void sendMsg(String msg) throws Exception {
			logger.debug("sendMsg시작(msg=" + msg + ")");
			try {
				if (sender == null || !sender.isAvailable()) {
					throw new Exception("메시지를보낼수없습니다.");
				}

				byte[] DATA = new byte[357];
				setData(DATA, msg);
				bos.write(DATA);
				bos.flush();
				logger.debug("메시지를전송하였습니다.DATA=" + new String(DATA));
				byte[] deliveryAck = new byte[DELIVERY_SMS_LENGTH];
				bis.read(deliveryAck);
				String deliveryAckString = new String(deliveryAck);
				logger.debug("딜리버리에크메시지=" + deliveryAckString);
			} catch (Exception e) {
				logger.error("에러발생", e);
				cleanup();
				throw e;
			}
			logger.debug("sendMsg종료()");
		}

		/**
		 * 
		 */
		public synchronized void connect() {
			try {
				logger.debug("SenderRun시작(블락킹...)");
				byte[] bindAck = new byte[BIND_ACK_LENGTH];
				bis.read(bindAck);
				String bindAckString = new String(bindAck);
				logger.debug("bindAckString=" + bindAckString);
				if (bindAckString.length() >= BIND_ACK_LENGTH) {
					String msgtype = "";
					msgtype = bindAckString.subSequence(10, 14).toString();
					logger.debug("msgtype=" + msgtype);

					if (BIND_ACK.equals(msgtype)) {
						String result = bindAckString.subSequence(14, 18)
								.toString();
						logger.debug("result=" + result);
						if (RESULT_SUCCESS.equals(result)) {
							available = true;
							logger.debug("소켓이이용가능하게되었습니다.");
						} else {
							throw new Exception("바인드실패로인한종료");
						}
					} else {
						throw new Exception("바인드ACK가아님소켓종료");
					}
				}
				logger.debug("SenderRun종료()");
			} catch (Exception e) {
				logger.error("에러발생", e);
				cleanup();
			}
		}

		/**
		 * send소켓종료
		 */
		private synchronized void cleanup() {
			logger.debug("cleanup시작()");
			try {
				logger.debug("socket=" + socket);
				if (socket != null && socket.isConnected()) {
					socket.close();
					logger.debug("socket을종료하였습니다.");
				}
				sender = null;
			} catch (Exception ex) {
				logger.error("에러발생", ex);
			}
			logger.debug("cleanup종료()");
		}

		public boolean isAvailable() {
			return available && socket.isConnected();
		}

	}

	/**
	 * @author nadir93
	 * @date 2014. 6. 24.
	 */
	class RecvChannelHandler implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.debug("recvChannel처리시작()");
			logger.debug("recvServer=" + recvServer);
			// logger.debug("recvSocket=" + recvSocket);
			try {
				if (recvServer == null) {
					initRecvServer();
				}
				receiver.sendPING();
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
			logger.debug("recvChannel처리종료()");
		}
	}

	/**
	 * socket blocking
	 * 
	 * @throws Exception
	 */
	private void initRecvServer() throws Exception {
		logger.debug("initRecvServer시작()");
		if (recvServer != null) {
			logger.debug("recvServer가널값이아닙니다.");
			return;
		}
		logger.debug("리시버서버 포트=" + RECEIVERPORT);
		recvServer = new ServerSocket(RECEIVERPORT);
		logger.debug("소케연결기다림..(블락킹)");
		Socket recvSocket = recvServer.accept();
		logger.debug("리시브소켓이연결되었습니다.recvSocket=" + recvSocket);
		receiver = new Receiver(recvSocket);
		receiver.start();
		logger.debug("리시버가시작되었습니다. receiver=" + receiver);
		logger.debug("initRecvServer종료()");
	}

	class Receiver extends Thread {
		private Socket socket;
		private BufferedInputStream bis;
		private BufferedOutputStream bos;

		public Receiver(Socket recvSocket) throws Exception {
			logger.debug("Receiver생성자시작(recvSocket=" + recvSocket + ")");
			socket = recvSocket;
			bis = new BufferedInputStream(socket.getInputStream());
			bos = new BufferedOutputStream(socket.getOutputStream());
			logger.debug("Receiver생성자종료(this=" + this + ")");
		}

		public void sendPING() throws Exception {
			logger.debug("sendPING시작(리시버)");
			try {
				// healthCheck
				logger.debug("socket=" + socket);
				if (socket.isConnected()) {
					setAliveServerData(bos);
					bos.flush();
					logger.debug("AliveCheck메시지를전송하였습니다.");
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
				cleanup();
			}
			logger.debug("sendPING종료(리시버)");
		}

		@Override
		public void run() {
			try {
				while (true) {
					logger.debug("receiverRun시작(블락킹...)");
					byte[] bindAck = new byte[357];
					bis.read(bindAck);
					String msg = new String(bindAck);
					logger.debug("받은메시지(리시버)=" + msg);
					if (!msg.trim().equals("")) {
						// 내용이 있으면 메시지로
						// new DeliverySMS(msg).start();
					} else {
						// 내용이 없으면 소켓 강제로 닫고 다시 시작..
						logger.error("데이타가존재하지않습니다.");
						throw new Exception("데이타가존재하지않습니다.");
					}
					logger.debug("receiverRun종료()");
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
				cleanup();
			}
		}

		/**
		 * 리시브소켓서버종료
		 */
		private void cleanup() {
			logger.debug("cleanup시작()");
			try {
				logger.debug("socket=" + socket);
				if (socket != null && socket.isConnected()) {
					socket.close();
					logger.debug("socket을종료하였습니다.");
				}
				logger.debug("recvServer=" + recvServer);
				if (recvServer != null && !recvServer.isClosed()) {
					recvServer.close();
					logger.debug("recvServer를종료하였습니다.");
				}
				recvServer = null;
			} catch (Exception ex) {
				logger.error("에러발생", ex);
			}
			logger.debug("cleanup종료()");
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
		logger.debug("setAliveServerData시작(bos=" + bos + ")");
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
		logger.debug("setAliveServerData종료()");
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
		logger.debug("setData시작(arryByte,=" + arryByte + ",str=" + str + ")");
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
		logger.debug("setData종료()");
	}

	/**
	 * 바인드 시 데이터 채우기
	 * 
	 * @param bos
	 * @throws IOException
	 */
	private static void setBindServerData(BufferedOutputStream bos)
			throws IOException {
		logger.debug("setBindServerData시작(bos=" + bos + ")");
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
		logger.debug("setBindServerData종료()");
	}

}
