/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

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
	List<Map<String, Object>> getMonthSummary(Map<String, String> params);

}
