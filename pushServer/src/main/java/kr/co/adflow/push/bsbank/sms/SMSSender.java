package kr.co.adflow.push.bsbank.sms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

import kr.co.adflow.push.bsbank.dao.impl.SMSDaoImpl;
import kr.co.adflow.push.bsbank.handler.SMSChannelHandler;

import org.slf4j.LoggerFactory;

public class SMSSender {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SMSSender.class);

	private Socket socket;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	private boolean available = false;

	private SMSChannelHandler handler;

	public SMSSender(Socket socket) throws Exception {
		logger.debug("Sender생성자시작(socket=" + socket + ")");
		this.socket = socket;
		bis = new BufferedInputStream(socket.getInputStream());
		bos = new BufferedOutputStream(socket.getOutputStream());

		// 기간계 시스템은 tcp/ip connect 성공즉시 bind 요청을 수행하여야 한다.
		// bind 요청은 최초 connect 접속시 한번만 수행하면 된다.
		SMSDaoImpl.setBindServerData(bos);
		bos.flush();
		logger.debug("bind메시지가전송되었습니다.");
		logger.debug("Sender생성자종료(this=" + this + ")");
	}

	public SMSSender(SMSChannelHandler handler, Socket socket) throws Exception {
		this(socket);
		this.handler = handler;
	}

	public synchronized void sendPING() throws Exception {
		logger.debug("sendPING시작(샌더)");
		try {
			// healthCheck
			logger.debug("socket=" + socket);
			if (socket.isConnected()) {
				SMSDaoImpl.setAliveServerData(bos);
				bos.flush();
				logger.debug("AliveCheck메시지를전송하였습니다.");
				byte[] aliveAck = new byte[SMSDaoImpl.ALIVE_ACK_LENGTH];
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
	 * @param msg2
	 * @throws Exception
	 */
	public synchronized void sendMsg(String phoneNum, String callbackNum,
			String msg) throws Exception {
		logger.debug("sendMsg시작(phoneNum=" + phoneNum + ",callbackNum="
				+ callbackNum + ", msg=" + msg + ")");
		try {
			if (!isAvailable()) {
				throw new Exception("메시지를보낼수없습니다.");
			}

			SMSDaoImpl.setDeliverSmsData(bos, "", phoneNum, callbackNum, msg);
			bos.flush();
			logger.debug("SMS메시지를전송하였습니다.phone=" + phoneNum); // 수정요망
			byte[] deliveryAck = new byte[SMSDaoImpl.DELIVERY_SMS_LENGTH];
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
			byte[] bindAck = new byte[SMSDaoImpl.BIND_ACK_LENGTH];
			bis.read(bindAck);
			String bindAckString = new String(bindAck);
			logger.debug("bindAckString=" + bindAckString);
			if (bindAckString.length() >= SMSDaoImpl.BIND_ACK_LENGTH) {
				String msgtype = "";
				msgtype = bindAckString.subSequence(10, 14).toString();
				logger.debug("msgtype=" + msgtype);

				if (SMSDaoImpl.BIND_ACK.equals(msgtype)) {
					String result = bindAckString.subSequence(14, 18)
							.toString();
					logger.debug("result=" + result);
					if (SMSDaoImpl.RESULT_SUCCESS.equals(result)) {
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
			handler.setSMSSender(null);
		} catch (Exception ex) {
			logger.error("에러발생", ex);
		}
		logger.debug("cleanup종료()");
	}

	public boolean isAvailable() {
		return available && socket.isConnected();
	}

}
