/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.AddressReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.TemplateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Address;
import kr.co.adflow.pms.domain.AddressParams;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgIdsParams;
import kr.co.adflow.pms.domain.MsgParams;
import kr.co.adflow.pms.domain.Template;
import kr.co.adflow.pms.domain.TemplateParams;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.AddressMapper;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.SummaryMapper;
import kr.co.adflow.pms.domain.mapper.TemplateMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.domain.push.mapper.ValidationMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class SvcServiceImpl.
 */
@Service
public class SvcServiceImpl implements SvcService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SvcServiceImpl.class);

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/** The validation mapper. */
	@Autowired
	private ValidationMapper validationMapper;
	
	/** The Address mapper. */
	@Autowired
	private AddressMapper addressMapper;
	
	/** The Template mapper. */
	@Autowired
	private TemplateMapper templateMapper;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The summary mapper. */
	@Autowired
	private SummaryMapper summaryMapper;

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getSvcMessageList(java.util.Map)
	 */
	@Override
	public MessagesRes getSvcMessageList(Map<String, String> params)  throws Exception {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId((String) params
				.get("appKey"));

		if (params.get("cSearchDate") == null) {
			// error
//			throw new RuntimeException("");
			throw new PmsRuntimeException("Search Date is null");
		}

		MsgParams msgParams = new MsgParams();
		
		//관제 메세지 only msg_type : 10
		msgParams.setMsgType(10);

		msgParams.setKeyMon(params.get("cSearchDate"));
		msgParams.setIssueId(issueId);
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		msgParams.setStatusArray(this.getStringArray(params
				.get("cSearchStatus")));

		String filter = params.get("cSearchFilter");
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}

		int cnt = messageMapper.getSysMessageListCnt(msgParams);
		logger.info("cnt :::::::{}", cnt);

		List<Message> list = messageMapper.getSysMessageList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getSvcMessageList(java.util.Map)
	 */
	@Override
	public MessagesRes getSvcMessageList2(Map<String, String> params)  throws Exception {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId((String) params
				.get("appKey"));

		if (params.get("cSearchDate") == null) {
			// error
//			throw new RuntimeException("");
			throw new PmsRuntimeException("Search Date is null");
		}

		MsgParams msgParams = new MsgParams();
		
		//관제 메세지 only msg_type : 10
		msgParams.setMsgType(10);

		msgParams.setKeyMon(params.get("cSearchDate"));
		msgParams.setIssueId(issueId);
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		msgParams.setStatusArray(this.getStringArray(params
				.get("cSearchStatus")));

		String filter = params.get("cSearchFilter");
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}

		int cnt = messageMapper.getSvcMessageListCnt(msgParams);
		logger.info("cnt :::::::{}", cnt);

		List<Message> list = messageMapper.getSvcMessageList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getSvcMessageList(java.util.Map)
	 */
	@Override
	public MessagesRes getSvcMessageDetailList(String msgId, String keyMon)  throws Exception {

		MessagesRes res = null;
		MsgParams msgParams = new MsgParams();
		msgParams.setMsgId(msgId);
		msgParams.setKeyMon(keyMon);

		List<Message> list = messageMapper.getSvcMessageDetailList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setData(list);

		return res;
	}

	/**
	 * Gets the string array.
	 *
	 * @param string the string
	 * @return the string array
	 */
	private String[] getStringArray(String string) {
		if ("ALL".equals(string)) {
			return null;
		}
		return string.split(",");
	}

	/**
	 * Gets the date.
	 *
	 * @param string the string
	 * @return the date
	 */
	private Date getDate(String string) {
		return DateUtil.fromISODateString(string);
	}

	/**
	 * Gets the int.
	 *
	 * @param string the string
	 * @return the int
	 */
	private int getInt(String string) {
		return Integer.parseInt(string);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getSvcResevationMessageList(java.util.Map)
	 */
	@Override
	public MessagesRes getSvcResevationMessageList(Map<String, String> params) {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId(params
				.get("appKey"));

//		if (params.get("cSearchDate") == null) {
//			// error
//			throw new RuntimeException("");
//		}


		MsgParams msgParams = new MsgParams();
		
		//관제 메세지 only msg_type : 10
		msgParams.setMsgType(10);

		msgParams.setKeyMon(params.get("cSearchDate"));
		msgParams.setIssueId(issueId);

		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		// 예약 발송 예정만 처리?
		// msgParams.setStatusArray(this.getStringArray(params.get("cSearchStatus")));

		String filter = params.get("cSearchFilter");
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}

		int cnt = messageMapper.getSvcResevationMessageListCnt(msgParams);
		logger.info("cnt :::::::{}", cnt);

		List<Message> list = messageMapper
				.getSvcResevationMessageList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#cancelReservationList(java.lang.String, kr.co.adflow.pms.adm.request.ReservationCancelReq)
	 */
	@Override
	public int cancelReservationList(String appKey, ReservationCancelReq reqIds) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

//		MsgIdsParams params = new MsgIdsParams();
//		params.setKeyMon(this.getKeyMon(reqIds.getMsgIds()));
//		params.setMsgIds(reqIds.getMsgIds());
//		params.setIssueId(null);
//		params.setUpdateId(issueId);
//		int cnt = messageMapper.cancelReservationList(params);
		
		List<Message> list;
		Message msg;
		String keyMon = DateUtil.getYYYYMM();
		String[] msgIds = reqIds.getMsgIds();
		int cnt = 0;
		for (int i = 0; i < msgIds.length; i++) {

			logger.debug("msgIds[i]::"+msgIds[i]+"==");
			list = messageMapper.selectReservationMessage(msgIds[i]);
			if (list.size() == 0) {
				continue;
			}
			msg = list.get(0);
			
			logger.debug("msg::"+msg.toString());
			msg.setKeyMon(keyMon);
			msg.setStatus(StaticConfig.MESSAGE_STATUS_RESEVATION_CANCEL);
			msg.setUpdateId(issueId);
			messageMapper.insertMessageRV(msg);
			messageMapper.insertContent(msg);
			messageMapper.deleteReservationMessage(msgIds[i]);
			cnt++;
			
		}
		return cnt;
	}

	/**
	 * Gets the key mon.
	 *
	 * @param msgIds the msg ids
	 * @return the key mon
	 */
	private String getKeyMon(String[] msgIds) throws Exception{
		if (msgIds.length < 1) {
//			throw new RuntimeException("msgId not found");
			throw new PmsRuntimeException("msgId not found");
		}
		return msgIds[0].substring(0, 6);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getMonthSummary(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getMonthSummary(Map<String, String> params,
			String appKey,
			String keyMon) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		MsgParams msgParams = new MsgParams();
		//관제 메세지 only msg_type : 10
		msgParams.setMsgType(10);
		
		msgParams.setIssueId(issueId);
		msgParams.setKeyMon(keyMon);
		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));


		return summaryMapper.getMonthSummary(msgParams);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#addAdress(java.lang.String, kr.co.adflow.pms.adm.request.AddressReq)
	 */
	@Override
	public int addAdress(String appKey,AddressReq addr) {
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		Address address = new Address();
		address.setUserId(issueId);
		address.setUfmi(addr.getUfmi());
		address.setUfmiName(addr.getUfmiName());
		address.setItem1(addr.getItem1());
		address.setItem2(addr.getItem2());
		address.setItem3(addr.getItem3());
		address.setIssueId(issueId);
		
		int insertCnt = addressMapper.insertAddress(address);
		
		return insertCnt;
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#updateAdress(java.lang.String, kr.co.adflow.pms.adm.request.AddressReq)
	 */
	@Override
	public int updateAdress(String appKey,AddressReq addr) throws Exception {
		String issueId = interceptMapper.selectCashedUserId(appKey);

		AddressParams addressParams = new AddressParams();
		addressParams.setUserId(issueId);
		addressParams.setUfmi(addr.getUfmi());
		
		Address address = addressMapper.selectAddress(addressParams);
		
	
		if (address == null) {
			throw new PmsRuntimeException("Ufmi not found");
			
		} else {
			
			address.setUfmiName(addr.getUfmiName());
			address.setItem1(addr.getItem1());
			address.setItem2(addr.getItem2());
			address.setItem3(addr.getItem3());
			address.setIssueId(issueId);
		}
		
		return addressMapper.updateAddress(address);
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#deleteAddress(java.lang.String, java.lang.String)
	 */
	@Override
	public int deleteAddress(String appKey, String ufmi) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		AddressParams addressParams = new AddressParams();
		addressParams.setUserId(issueId);
		addressParams.setUfmi(ufmi);

		return addressMapper.deleteAddress(addressParams);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getAddressList()
	 */
	@Override
	public List<Address> getAddressList(String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);
		List<Address> resultAddress = null;
		resultAddress = addressMapper.selectAddressList(issueId);

		return resultAddress;
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#addTemplate(java.lang.String, kr.co.adflow.pms.adm.request.TemplateReq)
	 */
	@Override
	public int addTemplate(String appKey,TemplateReq tempReq) {
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		Template template = new Template();
		template.setUserId(issueId);
		//KeyGenerator
		template.setTemplateId(KeyGenerator.generateMsgId());
		template.setTemplateName(tempReq.getTemplateName());
		template.setTemplateMsg(tempReq.getTemplateMsg());
		template.setIssueId(issueId);
		
		int insertCnt = templateMapper.insertTemplate(template);
		
		return insertCnt;
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#updateTemplate(java.lang.String, kr.co.adflow.pms.adm.request.TemplateReq)
	 */
	@Override
	public int updateTemplate(String appKey,TemplateReq tempReq) throws Exception {
		String issueId = interceptMapper.selectCashedUserId(appKey);

		TemplateParams templateParams = new TemplateParams();
		templateParams.setUserId(issueId);
		templateParams.setTemplateId(tempReq.getTemplateId());
		
		Template template = templateMapper.selectTemplate(templateParams);
		
	
		if (template == null) {
			throw new PmsRuntimeException("template not found");
			
		} else {
			
			template.setTemplateName(tempReq.getTemplateName());;
			template.setTemplateMsg(tempReq.getTemplateMsg());;
			template.setIssueId(issueId);
		}
		
		return templateMapper.updateTemplate(template);
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#deleteTemplate(java.lang.String, java.lang.String)
	 */
	@Override
	public int deleteTemplate(String appKey, String templateId) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		TemplateParams templateParams = new TemplateParams();
		templateParams.setUserId(issueId);
		templateParams.setTemplateId(templateId);

		return templateMapper.deleteTemplate(templateParams);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SvcService#getTemplateList()
	 */
	@Override
	public List<Template> getTemplateList(String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);
		List<Template> resultTemplate = null;
		resultTemplate = templateMapper.selectTemplateList(issueId);

		return resultTemplate;
	}

}
