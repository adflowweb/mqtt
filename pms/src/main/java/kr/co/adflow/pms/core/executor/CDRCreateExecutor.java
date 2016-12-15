/*
 * 
 */
package kr.co.adflow.pms.core.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.handler.ZookeeperHandler;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.RecordFomatUtil;
import kr.co.adflow.pms.domain.CDR;
import kr.co.adflow.pms.domain.CDRParams;
import kr.co.adflow.pms.domain.mapper.CDRMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.domain.push.mapper.PushMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.mq.pcf.PCFMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class CDRCreateExecutor.
 */
@Component("cDRCreateExecutor")
public class CDRCreateExecutor {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CDRCreateExecutor.class);

	/** The CDR mapper. */
	@Autowired
	private CDRMapper cDRMapper;

	/** The Push Mapper. */
	@Autowired
	private PushMapper pushMapper;

	/** The User Mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The Pms Config. */
	@Autowired
	private PmsConfig pmsConfig;

	private int fileNo;
	private String fileDate;
	private String filePath;
	private int totalRow;
	private FileWriter fw;

	@Autowired
	ZookeeperHandler zookeeperHandler;

	/**
	 * Creates the CDR.
	 */
	public void createCDR() {
		if(!zookeeperHandler.getLeader()){
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "createCDR() 나는 리더가 아닙니다.");
			return;
		} else{
			
		  
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "createCDR() 나는 리더 입니다.");
		int startRow = 0;
		this.fileNo = 0;
		this.totalRow = 0;

		// Today
		String endDate = DateUtil.getYYYYMMDD(0);
		// Yesterday
		String startDate = DateUtil.getYYYYMMDD(-1);

		this.fileDate = startDate;

		CDRParams cDRParams = new CDRParams();

		cDRParams.setKeyMon(startDate.substring(0, 6));
		cDRParams.setStartDate(startDate);
		cDRParams.setEndDate(endDate);
		cDRParams.setStartRow(startRow);
		cDRParams.setLengthRow(0);

		this.createCDRFile(cDRParams);
		}
	}

	/**
	 * Creates the CDR.
	 */
	public int createCDR(String date) throws Exception {
		if(!zookeeperHandler.getLeader()){
			logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "createCDR() 나는 리더가 아닙니다.");
			return 0;
		} else{
			
		
		logger.debug(pmsConfig.EXECUTOR_SERVER_ID1 + "createCDR() 나는 리더 입니다.");
		logger.debug("cdr 생성시작 ");

		int startRow = 0;
		this.fileNo = 0;
		this.totalRow = 0;

		// Today
		String endDate = DateUtil.getYYYYMMDD(date, 1);
		// Yesterday
		String startDate = date;
		this.fileDate = startDate;

		CDRParams cDRParams = new CDRParams();

		cDRParams.setKeyMon(startDate.substring(0, 6));
		cDRParams.setStartDate(startDate);
		cDRParams.setEndDate(endDate);
		cDRParams.setStartRow(startRow);
		cDRParams.setLengthRow(0);

		return this.createCDRFile(cDRParams);
		}
	}

	/**
	 * Creates the CDR file.
	 */
	public int createCDRFile(CDRParams cDRParams) {
		logger.debug("createCDRFile 시작");

		this.fw = null;
		logger.info("start ::{}", cDRParams.getStartDate());
		logger.info("end ::{}", cDRParams.getEndDate());

		int cDRFileMaxRow = pmsConfig.CDR_FILE_MAX_ROW;
		int cDRDbMaxRow = pmsConfig.CDR_DB_MAX_ROW;

		// CDR Print Count
		int cDRPrintCnt = 0;
		// CDR Total Count
		Integer cDRTotCnt = 0;
		// CDR Count
		int cDRCnt = 0;

		boolean result = false;

		try {

			long start;
			long stop;
			start = System.currentTimeMillis();

			cDRTotCnt = cDRMapper.getCDRListTotalCnt2(cDRParams);

			if (cDRTotCnt == null) {
				cDRTotCnt = 0;
			}
			logger.debug("cdrtotalCnt:" + cDRTotCnt);

			if (cDRTotCnt > 0) {
				boolean cDRFileNext = true;

				this.headerPrint();

				// 1000건씩 처리
				cDRParams.setLengthRow(pmsConfig.CDR_DB_MAX_ROW);
				for (int j = 0; j < cDRTotCnt; j = j + pmsConfig.CDR_DB_MAX_ROW) {

					cDRParams.setStartRow(j + cDRPrintCnt);
					this.cDRPrint(cDRParams);

				}

				// Tail print
				this.tailPrint();
			} else {

				logger.debug("==   CDR NO DATA  ==");
				// Header print
				this.headerPrint();
				// Tail print
				this.tailPrint();
			}
			stop = System.currentTimeMillis();
			logger.debug("CDR file create elapsedTime=" + (stop - start) + "ms");

			// this.cDRCopyCommand();
			this.cDRFileCopy();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.fw != null) {
					this.fw.close();
				}
			} catch (Exception e) {
				logger.error("file write close Error ::", e);
				e.printStackTrace();
			}

		}

		return cDRTotCnt;
	}

	// tail PRINT
	public void tailPrint() throws IOException {
		logger.debug("tailPrint Start");
		String tailRecordType = "T";
		String tailCDRCount;
		String tailNull = RecordFomatUtil.stingFormat("", 69);
		StringBuffer recordSb;

		tailCDRCount = RecordFomatUtil.intFormat(this.totalRow, 13);
		recordSb = new StringBuffer();
		recordSb.append(tailRecordType);
		recordSb.append(tailCDRCount);
		recordSb.append(tailNull);

		// logger.info("CDR : [{}]:tail", recordSb.toString());
		try {
			this.fw.write(recordSb.toString());
			this.fw.close();
		} catch (IOException e) {
			logger.error("Tail file write Error::");
			throw e;
		}
		logger.debug("tailPrint End");
	}

	// header PRINT
	public void headerPrint() throws IOException {
		logger.debug("CDR Header Print 시작");

		File file = null;
		File directory = null;

		String fileNamePrefix = "EM";
		StringBuffer fileName = null;

		// CDR file create
		fileName = new StringBuffer();
		fileName.append(fileNamePrefix);
		fileName.append(this.fileDate);
		fileName.append(RecordFomatUtil.intFormat(this.fileNo, 3));
		fileName.append(".DAT");
		logger.debug("CDR Header Print step..1");
		directory = new File(pmsConfig.CDR_FILE_PATH + this.fileDate);
		if (!directory.isDirectory()) {
			directory.mkdir();
			logger.debug("{} directory mkdir Ok", directory.getName());
		}
		logger.debug("CDR Header Print step..2");

		file = new File(directory.getPath() + "/" + fileName.toString());

		this.fw = new FileWriter(file);
		logger.debug("CDR Header Print step..3");
		String headerRecordType = "H";
		String headerNull = RecordFomatUtil.stingFormat("", 82);
		StringBuffer recordSb;

		recordSb = new StringBuffer();
		recordSb.append(headerRecordType);
		recordSb.append(headerNull);
		recordSb.append("\n");
		logger.debug("CDR Header Print step..4");
		// logger.info("CDR : [{}]:header", recordSb.toString());
		try {
			this.fw.write(recordSb.toString());
			logger.debug("CDR Header Print step..5");
		} catch (IOException e) {
			logger.error("Header file write Error::");
			throw e;
		}

		// file sequece가 999이면 다음 sequece은 0으로 순
		if (fileNo == 999) {
			fileNo = 0;
		} else {
			fileNo++;
		}
		logger.debug("CDR Header Print End..");
	}

	// CDR PRINT
	public void cDRPrint(CDRParams cDRParams) throws IOException {
		logger.debug("CDR Print 시작 ");

		StringBuffer recordSb;

		try {

			List<CDR> list = cDRMapper.getCDRList2(cDRParams);
			logger.debug("CDR Print step 1");
			// CDR PRINT
			String messageTopic1 = "";
			String messageTopic2 = "";
			String callerNo, calledNo;
			String tempNo;
			String pTalkVer = "";
			String rRCause = "01";
			int tempIndex;
			boolean groupYes = false;

			for (CDR cDR : list) {
				// stringBuffer clear
				logger.debug("CDR Print step 2..in for");
				recordSb = new StringBuffer();

				if (cDR.getMsgType() == 10) {
					// web message
					// sender 권한이 "svcadm"이면 skip.
					if (userMapper.selectRole(cDR.getIssueId()).equals("svcadm")) {
						continue;
					}
				}

				// group message check
				if (cDR.getGroupId() == null || cDR.getGroupId().indexOf("/") < 0) {
					groupYes = false;
				} else {
					groupYes = true;
				}
				logger.debug("CDR Print step 3..in for");
				// Record Type
				recordSb.append("C");

				// + Transaction Type - group message check
				if (groupYes) {
					recordSb.append("GM");
				} else {
					recordSb.append("PM");
				}

				// Time of Send Request
				if (cDR.getUpdateTime() == null) {
					recordSb.append(RecordFomatUtil.stingFormat("", 14));
				} else {
					recordSb.append(RecordFomatUtil.stingFormat(DateUtil.getDateTime(cDR.getUpdateTime()), 14));
				}

				logger.debug("CDR Print step 4..in for");
				// Time of Received Request
				if (cDR.getPmaAckTime() == null) {
					recordSb.append(RecordFomatUtil.stingFormat("", 14));
					rRCause = "01";
				} else {
					logger.debug("업데이트 시간(실제 발송시간):" + cDR.getUpdateTime());
					logger.debug("pma akc 시간:" + cDR.getPmaAckTime());
					if (cDR.getPmaAckTime().getTime() >= cDR.getUpdateTime().getTime()) {
						logger.debug("ack 도착 시간이 정상");
						recordSb.append(RecordFomatUtil.stingFormat(DateUtil.getDateTime(cDR.getPmaAckTime()), 14));
					} else {
						logger.debug("ack 도착 시간이 정상이 아닌경우");
						recordSb.append(RecordFomatUtil.stingFormat(DateUtil.getDateTime(cDR.getIssueTime()), 14));
					}

					rRCause = "00";
				}

				logger.debug("CDR Print step 5..in for");
				// Caller NO
				if (cDR.getMsgType() == 106) {
					// user message
					callerNo = RecordFomatUtil.topicToUfmiNo(cDR.getIssueId());
					recordSb.append(RecordFomatUtil.stingFormat(callerNo, 13));
					pTalkVer = cDR.getIssueId().substring(5, 6);

				} else {
					// web message
					// System.out.println("========
					// cDR.getIssueId()::"+cDR.getIssueId()+",
					// cDR.getMsgType()::"+cDR.getMsgType());
					callerNo = userMapper.selectUfmi(cDR.getIssueId());
					// System.out.println("======== callerNo::"+callerNo);
					if (callerNo == null || callerNo.trim().length() <= 0) {
						callerNo = "";
						rRCause = "03";
					} else {
						callerNo = RecordFomatUtil.ufmiNo(callerNo);
					}

					// System.out.println("======== callerNo::"+callerNo);
					recordSb.append(RecordFomatUtil.stingFormat(callerNo, 13));

					// if (callerNo.substring(0, 2).equals("82")) {
					// pTalkVer = "1";
					// } else {
					// pTalkVer = "2";
					// }
				}

				logger.debug("CDR Print step 6..in for");
				// Called NO - Group Msg NO : ReceiverTopic, Group Msg Yes :
				// ReceiverUfmi
				calledNo = pushMapper.getUfmi(cDR.getTokenId());

				if (calledNo != null && calledNo.trim().length() > 0) {
					calledNo = RecordFomatUtil.ufmiNo(calledNo);
				} else {
					calledNo = "";
				}

				// 20150508 - 수신자번호가 없으면 RR Cause = "02"
				// 20150519 - 송신번호가 없으면 RR Cause = "03", 수신자번호, 송신번호 둘다 없으면 RR
				// Cause = "04"
				if (rRCause.equals("03")) {
					if (calledNo.equals("")) {
						rRCause = "04";
					}

				} else {
					if (calledNo.equals("")) {
						rRCause = "02";
					}
				}

				recordSb.append(RecordFomatUtil.stingFormat(calledNo, 13));
				// RR Cause
				recordSb.append(rRCause);
				// Message Type
				recordSb.append(RecordFomatUtil.intFormat(cDR.getMediaType(), 2));
				// Send Terminal Type
				if (cDR.getMsgType() == 106) {
					recordSb.append(RecordFomatUtil.intFormat(Integer.parseInt(pTalkVer), 2));
				} else {
					recordSb.append(RecordFomatUtil.intFormat(cDR.getSendTerminalType(), 2));
				}

				// Packet Size
				// 2015-05-19 - Message Size 0 이면 1로 출력 요청함.
				if (cDR.getMsgSize() == 0) {
					cDR.setMsgSize(1);
				}
				recordSb.append(RecordFomatUtil.intFormat(cDR.getMsgSize(), 11));

				logger.debug("CDR Print step 7..in for");
				if (groupYes) {
					// 그룹메세지에서 자신은 skip
					if (callerNo.equals(calledNo)) {

						logger.debug("======== skip::" + callerNo);
						continue;
					}
					pTalkVer = cDR.getGroupId().substring(5, 6);
					tempNo = cDR.getGroupId().substring(10, cDR.getGroupId().length());
					tempIndex = tempNo.indexOf("/");

					// message topic1 Ptalk1.0, Ptalk2.0 check
					if (pTalkVer.equals("1")) {
						messageTopic1 = RecordFomatUtil
								.intFormat(Integer.parseInt(tempNo.substring(0, tempIndex).replace("b", "")), 4);
					} else if (pTalkVer.equals("2")) {
						messageTopic1 = RecordFomatUtil
								.intFormat(Integer.parseInt(tempNo.substring(0, tempIndex).replace("b", "")), 5);
					} else {
						messageTopic1 = "";
					}

					messageTopic2 = RecordFomatUtil.intFormat(
							Integer.parseInt(
									tempNo.substring(tempIndex + 1, tempNo.length()).replace("g", "").replace("/", "")),
							3);
					// Message Topic 1
					recordSb.append(RecordFomatUtil.stingFormat(messageTopic1, 6));
					// Message Topic 2
					recordSb.append(messageTopic2);

				} else {
					// Message Topic 1
					recordSb.append(RecordFomatUtil.stingFormat("", 6));
					// Message Topic 2
					recordSb.append(RecordFomatUtil.stingFormat("", 3));
				}
				recordSb.append("\n");

				// tempI++;
				// logger.info("CDR : [{}]:{}", recordSb.toString(),tempI);

				if (totalRow >= pmsConfig.CDR_FILE_MAX_ROW) {
					logger.debug("CDR Print step 8..in for");
					this.tailPrint();
					this.headerPrint();
					this.fw.write(recordSb.toString());
					totalRow = 1;

				} else {
					logger.debug("CDR Print step 9..in for");
					this.fw.write(recordSb.toString());
					totalRow++;
				}

			}
		} catch (IOException ioe) {
			logger.error("CDR file write Error::");
			throw ioe;
		} catch (Exception e) {
			logger.error("cDRPrint Error ::", e);
			e.printStackTrace();
		}

	}

	// public void cDRCopyCommand() {
	//
	// String path = pmsConfig.CDR_FILE_PATH+this.fileDate;
	// logger.info("path ::{}", path);
	//
	// String targetDir = pmsConfig.CDR_TARGETFILE_PATH;
	// String shellDir = pmsConfig.CDR_FILE_PATH;
	//
	//// String command = "cp
	// /Users/gwang/Desktop/dev/workspace_PUSH_KT_V2/temp/*
	// /Users/gwang/Desktop/dev/workspace_PUSH_KT_V2/temp/cdr";
	// String command = shellDir + "copyCDRFile.sh"+ " 123 " + path +"/* " +
	// targetDir;
	//
	// logger.info("==== shell command ::{}", command);
	//
	// java.lang.Runtime runTime = java.lang.Runtime.getRuntime();
	// java.lang.Process process;
	// try {
	// process = runTime.exec(command);
	//
	// process.waitFor();
	// StringBuffer output = new StringBuffer();
	//
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(process.getInputStream()));
	//
	// String line = "";
	// while ((line = reader.readLine())!= null) {
	// output.append(line + "\n");
	// }
	//
	// logger.info("==== command shell result ::{}", output.toString());
	//
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	//
	// }

	public void cDRFileCopy() {

		File directory = new File(pmsConfig.CDR_FILE_PATH + this.fileDate);

		File[] fileList = directory.listFiles();

		String targetDir = pmsConfig.CDR_TARGETFILE_PATH;

		String fileName;

		// 20150512 - 기존 파일 삭제 하지 말라고 요청함.
		// File targetDirFile = new File(targetDir);
		//
		// File[] delFileList = targetDirFile.listFiles();
		//
		//
		// //file delete
		// for (int i = 0; i < delFileList.length; i++) {
		//
		// delFileList[i].delete();
		// }

		// file copy
		for (int i = 0; i < fileList.length; i++) {

			fileName = fileList[i].getName();
			File fTarget = new File(targetDir + fileName);
			fileCopy(fileList[i], fTarget);
		}

	}

	public void fileCopy(File fOrg, File fTarget) {
		FileInputStream fI = null;
		FileOutputStream fO = null;
		try {
			fI = new FileInputStream(fOrg);
			if (!fTarget.isFile()) {
				File fParent = new File(fTarget.getParent());
				if (!fParent.exists()) {
					fParent.mkdir();
				}
				fTarget.createNewFile();
			}

			fO = new FileOutputStream(fTarget);
			byte[] bBuffer = new byte[1024 * 8];
			int nRead;
			while ((nRead = fI.read(bBuffer)) != -1) {
				fO.write(bBuffer, 0, nRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fI != null) {
				try {
					fI.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (fO != null) {
				try {
					fO.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
