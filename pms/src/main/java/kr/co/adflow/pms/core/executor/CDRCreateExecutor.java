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
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.RecordFomatUtil;
import kr.co.adflow.pms.domain.CDR;
import kr.co.adflow.pms.domain.CDRParams;
import kr.co.adflow.pms.domain.mapper.CDRMapper;

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
	private static final Logger logger = LoggerFactory
			.getLogger(CDRCreateExecutor.class);

	/** The CDR mapper. */
	@Autowired
	private CDRMapper cDRMapper;
	
	/** The Pms Config. */
	@Autowired
	private PmsConfig pmsConfig;

	
	/**
	 * Creates the CDR.
	 */
	public void createCDR() {
		
		int startRow = 0;

		//Today
		String endDate = DateUtil.getYYYYMMDD(0);
		//Yesterday
		String startDate = DateUtil.getYYYYMMDD(-1);
		
		
		CDRParams cDRParams = new CDRParams();
		
		cDRParams.setKeyMon(startDate.substring(0, 6));
		cDRParams.setStartDate(startDate);
		cDRParams.setEndDate(endDate);
		cDRParams.setStartRow(startRow);
		cDRParams.setLengthRow(0);
		
		this.createCDRFile(cDRParams);

	}
	
	/**
	 * Creates the CDR.
	 */
	public int createCDR(String date) {

		
		int startRow = 0;

		//Today
		String endDate =DateUtil.getYYYYMMDD(date, 1);
		//Yesterday
		String startDate = date;
		
		CDRParams cDRParams = new CDRParams();
		
		cDRParams.setKeyMon(startDate.substring(0, 6));
		cDRParams.setStartDate(startDate);
		cDRParams.setEndDate(endDate);
		cDRParams.setStartRow(startRow);
		cDRParams.setLengthRow(0);

		return this.createCDRFile(cDRParams);
	}
	
	
	/**
	 * Creates the CDR file.
	 */
	public int createCDRFile(CDRParams cDRParams) {
		

//		logger.info("start ::{}", cDRParams.getStartDate());
//		logger.info("end ::{}", cDRParams.getStartDate());
		
		
		int cDRFileMaxRow = pmsConfig.CDR_FILE_MAX_ROW;
		int cDRDbMaxRow = pmsConfig.CDR_DB_MAX_ROW;
		
		// CDR Print Count
		int cDRPrintCnt=0;
		// CDR Total Count
		Integer cDRTotCnt = 0;
		// CDR Count
		int cDRCnt =0 ;
		
		FileWriter fw = null;
		File file = null;
		File directory = null;
		
		String fileNamePrefix = "EM";
		StringBuffer fileName = null;
		
		boolean result = false;
		
		try {
			
			long start;
			long stop;
			start = System.currentTimeMillis();

			cDRTotCnt = cDRMapper.getCDRListTotalCnt(cDRParams);
			
			if (cDRTotCnt == null) {
				cDRTotCnt = 0;
			}
			
			if (cDRTotCnt > 0) {
				boolean cDRFileNext = true;
				
				
				//30000 CDR Row => new file
				cDRParams.setLengthRow(cDRFileMaxRow);
				
				for (int i = 0; cDRFileNext; i++) {
					
					// CDR file create
					fileName = new StringBuffer();
					fileName.append(fileNamePrefix);
					fileName.append(cDRParams.getStartDate());
					fileName.append(RecordFomatUtil.intFormat(i, 3));
					fileName.append(".DAT");
					
					directory = new File(pmsConfig.CDR_FILE_PATH+cDRParams.getStartDate());
					if (!directory.isDirectory()) {
						directory.mkdir();
						logger.info("{} directory mkdir Ok",directory.getName());
					}
					
					file = new File(directory.getPath()+"/"+ fileName.toString());
					
					fw = new FileWriter(file);
					
					
					//Header print
					if (cDRTotCnt - cDRPrintCnt > cDRFileMaxRow) {
						cDRCnt = cDRFileMaxRow;
					} else {
						cDRCnt = cDRTotCnt - cDRPrintCnt;
					}
					this.headerPrint(fw, cDRCnt);
					
					//1000건씩 처리 
					cDRParams.setLengthRow(cDRDbMaxRow);
					for (int j = 0; j < cDRCnt; j = j + cDRParams.getLengthRow()) {
		
						cDRParams.setStartRow(j+cDRPrintCnt);
						this.cDRPrint(fw, cDRParams);

					}
					
					//Tail print
					this.tailPrint(fw);
					
					// Next Row Check
					cDRPrintCnt = cDRPrintCnt + cDRCnt;
					
					if (cDRTotCnt > cDRPrintCnt) {
						cDRParams.setStartRow(cDRPrintCnt);
						
						
						
						//file sequece가 999이면 다음  sequece은 0으로 순 
						if (i == 999) {
							i = 0;
						}
					}else {
						cDRFileNext = false;
					}

				}
				
			} else {
				
				// CDR file create
				fileName = new StringBuffer();
				fileName.append(fileNamePrefix);
				fileName.append(cDRParams.getStartDate());
				fileName.append(RecordFomatUtil.intFormat(0, 3));
				fileName.append(".DAT");
				
				directory = new File(pmsConfig.CDR_FILE_PATH+cDRParams.getStartDate());
				if (!directory.isDirectory()) {
					directory.mkdir();
					logger.info("{} directory mkdir Ok",directory.getName());
				}
				
				file = new File(directory.getPath()+"/"+ fileName.toString());
				
				fw = new FileWriter(file);
				
				logger.info("==   CDR NO DATA  ==");
				//Header print
				this.headerPrint(fw, cDRTotCnt);
				//Tail print
				this.tailPrint(fw);
			}
			
			logger.info("CDR file {} create Sucess", fileName.toString());
			
			
			stop = System.currentTimeMillis();
			System.out.println("CDR file create elapsedTime=" + (stop - start) + "ms");
			
			this.cDRCopyCommand(directory.getPath());
//			this.cDRFileCopy(directory);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fw != null) {
					fw.close();
				}
			} catch (Exception e) {
				logger.error("file write close Error ::", e);
				e.printStackTrace();
			}
			
		}
		
		return cDRTotCnt;
	}
	
	// Header PRINT
	public void headerPrint (FileWriter fw, int cDRSize) throws IOException {
		String headerRecordType = "H";
		String headerCDRCount;
		String headerNull = RecordFomatUtil.stingFormat("", 69);
		StringBuffer recordSb;

		headerCDRCount = RecordFomatUtil.intFormat(cDRSize, 13);
		recordSb = new StringBuffer();
		recordSb.append(headerRecordType);
		recordSb.append(headerCDRCount);
		recordSb.append(headerNull);
		recordSb.append("\n");
		

//		logger.info("CDR : [{}]:Header", recordSb.toString());
		try {
			fw.write(recordSb.toString());
		} catch (IOException e) {
			logger.error("Header file write Error::");
			throw e;
		}
		
	}
	
	// Tail PRINT
	public void tailPrint(FileWriter fw) throws IOException {
		String tailRecordType = "T";
		String tailNull = RecordFomatUtil.stingFormat("", 82);
		StringBuffer recordSb;

		
		recordSb = new StringBuffer();
		recordSb.append(tailRecordType);
		recordSb.append(tailNull);
		
//		logger.info("CDR : [{}]:Tail", recordSb.toString());
		try {
			fw.write(recordSb.toString());
			fw.close();
		} catch (IOException e) {
			logger.error("Tail file write Error::");
			throw e;
		}
		
	}
	
	// CDR PRINT
	public void cDRPrint(FileWriter fw, CDRParams cDRParams) throws IOException {
		
		StringBuffer recordSb;
		
		try {
			
			List<CDR> list = cDRMapper.getCDRList(cDRParams);
			
			// CDR PRINT
			String messageTopic1 = "";
			String messageTopic2 = "";
			String callerNo, calledNo;
			String tempNo, pTalkVer;
			int tempIndex;
			for (CDR cDR : list) {
				//stringBuffer clear
				recordSb = new StringBuffer();
				
				//Record Type 
				recordSb.append("C");
				
				//+ Transaction Type - group message check
				if (cDR.getGroupId() == null) {
					recordSb.append("PM");
				}else {
					recordSb.append("GM");
				}
				
				//Time of Send Request
				if (cDR.getUpdateTime() == null) {
					recordSb.append(RecordFomatUtil.stingFormat("", 14));
				} else {
					recordSb.append(RecordFomatUtil.stingFormat(DateUtil.getDateTime(cDR.getUpdateTime()), 14));
				}
				
				//Time of Received Request
				if (cDR.getPmaAckTime() == null) {
					recordSb.append(RecordFomatUtil.stingFormat("", 14));
				} else {
					recordSb.append(RecordFomatUtil.stingFormat(DateUtil.getDateTime(cDR.getPmaAckTime()), 14));
				}
				
				
				//Caller NO
				callerNo = RecordFomatUtil.topicToUfmiNo(cDR.getIssueId());
				recordSb.append(RecordFomatUtil.stingFormat(callerNo, 13));
				
				//Called NO - Group Msg NO : ReceiverTopic, Group Msg Yes : ReceiverUfmi
				if (cDR.getReceiverUfmi() != null && cDR.getReceiverUfmi().trim().length() > 0) {
					calledNo = RecordFomatUtil.ufmiNo(cDR.getReceiverUfmi());
				} else {
					
//					logger.info("=== getUpdateTime ==::{}",cDR.getUpdateTime());
					calledNo = RecordFomatUtil.topicToUfmiNo(cDR.getReceiverTopic());
				}
				
				recordSb.append(RecordFomatUtil.stingFormat(calledNo, 13));
				//RR Cause - MS_NORMAL
				recordSb.append("00");
				//Message Type
				recordSb.append(RecordFomatUtil.intFormat(cDR.getMediaType(), 2));
				//Send Terminal Type
				recordSb.append(RecordFomatUtil.intFormat(cDR.getSendTerminalType(), 2));
				//Packet Size

				recordSb.append(RecordFomatUtil.intFormat(cDR.getMsgSize(), 11));


				if (cDR.getGroupId() == null) {
					//Message Topic 1
					recordSb.append(RecordFomatUtil.stingFormat("", 6));
					//Message Topic 2
					recordSb.append(RecordFomatUtil.stingFormat("", 3));
					
				} else {
					pTalkVer = cDR.getGroupId().substring(5, 6);
					tempNo = cDR.getGroupId().substring(10, cDR.getGroupId().length());
					tempIndex = tempNo.indexOf("/");
					
					// message topic1 Ptalk1.0, Ptalk2.0 check
				    if (pTalkVer.equals("1")) {
				    	messageTopic1 = RecordFomatUtil.intFormat(Integer.parseInt(tempNo.substring(0, tempIndex).replace("b", "")), 4) ;
					} else if (pTalkVer.equals("2")) {
						messageTopic1 = RecordFomatUtil.intFormat(Integer.parseInt(tempNo.substring(0, tempIndex).replace("b", "")), 5) ;
					} else {
						messageTopic1 = "";
					}
					
					messageTopic2 = RecordFomatUtil.intFormat(Integer.parseInt(tempNo.substring(tempIndex+1, tempNo.length()).replace("g", "").replace("/", "")), 3);
					//Message Topic 1
					recordSb.append(RecordFomatUtil.stingFormat(messageTopic1, 6));
					//Message Topic 2
					recordSb.append(messageTopic2);

				}
				recordSb.append("\n");

//				tempI++;
//				logger.info("CDR : [{}]:{}", recordSb.toString(),tempI);

				fw.write(recordSb.toString());

			}
		} catch (IOException ioe) {
			logger.error("CDR file write Error::");
			throw ioe;	
		} catch (Exception e) {
			logger.error("cDRPrint Error ::", e);
			e.printStackTrace();
		}
		
	}
	
	public void cDRCopyCommand(String path) {
		
		logger.info("path ::{}", path);
		
		String targetDir = pmsConfig.CDR_TARGETFILE_PATH;
		String shellDir = pmsConfig.CDR_FILE_PATH;
		
//		String command = "cp /Users/gwang/Desktop/dev/workspace_PUSH_KT_V2/temp/* /Users/gwang/Desktop/dev/workspace_PUSH_KT_V2/temp/cdr";
		String command = shellDir + "copyCDRFile.sh"+ " 123 " + path +"/* " + targetDir;
		
		logger.info("==== shell command ::{}", command);

		java.lang.Runtime runTime = java.lang.Runtime.getRuntime();
		java.lang.Process process;
		try {
			process = runTime.exec(command);
			
			process.waitFor();
			StringBuffer output = new StringBuffer();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			 
            String line = "";           
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            
            logger.info("==== command shell result  ::{}", output.toString());
            

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
//	public void cDRFileCopy(File directory) {
//		
//		File[] fileList = directory.listFiles();
//		
//		String targetDir = pmsConfig.CDR_TARGETFILE_PATH;
//		
//		String fileName;
//		
//		File targetDirFile = new File(targetDir);
//		
//		File[] delFileList = targetDirFile.listFiles();
//		
//		
//		//file copy
//		for (int i = 0; i < delFileList.length; i++) {
//			
//			delFileList[i].delete();
//		}
//		
//		
//		//file copy
//		for (int i = 0; i < fileList.length; i++) {
//			
//			fileName = fileList[i].getName();
//			File fTarget = new File(targetDir+fileName);
//			fileCopy(fileList[i], fTarget);
//		}
//		
//		
//	}
//	
//	
//	public void fileCopy( File fOrg, File fTarget)
//	 {
//		FileInputStream fI = null;
//		FileOutputStream fO = null;
//		try {
//			fI = new FileInputStream ( fOrg);
//			  if ( !fTarget.isFile())
//			  {
//			   File fParent = new File ( fTarget.getParent());
//			   if ( !fParent.exists())
//			   {
//			    fParent.mkdir();
//			   }
//			   fTarget.createNewFile();
//			  }
//
//			  fO = new FileOutputStream ( fTarget);
//			  byte[] bBuffer = new  byte[ 1024 * 8];
//			  int nRead;
//			  while ( (nRead = fI.read( bBuffer)) != -1)
//			  {
//				  fO.write( bBuffer, 0, nRead);
//			  }
//
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(fI != null){
//				try {
//					fI.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			if(fO != null){
//				try {
//					fO.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//
//	 }

	

}
