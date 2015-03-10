/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.MsgParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface SummaryMapper.
 */
public interface SummaryMapper {

	/**
	 * Gets the month summary.
	 *
	 * @param params the params
	 * @return the month summary
	 */
	//List<Map<String, Object>> getMonthSummary(Map<String, String> params);
	List<Map<String, Object>> getMonthSummary(MsgParams params);
	
	
	
	double getTPS(String keyMon);

}
