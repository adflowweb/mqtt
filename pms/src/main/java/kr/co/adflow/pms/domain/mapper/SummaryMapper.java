package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;


public interface SummaryMapper {

	List<Map<String,Object>> getMonthSummary(Map<String,String> params);

}
