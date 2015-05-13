/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.CDR;
import kr.co.adflow.pms.domain.CDRParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface CDRMapper.
 */
public interface CDRMapper {

	/**
	 * Gets the CDR.
	 *
	 * @param params the params
	 * @return the CDR
	 */
	List<CDR> getCDRList(CDRParams cDRParams);
	
	/**
	 * Gets the CDR total count.
	 *
	 * @param params the params
	 * @return the CDR total count.
	 */
	int getCDRListTotalCnt(CDRParams cDRParams);
	
	// TEST 
	List<CDR> getCDRList2(CDRParams cDRParams);
	int getCDRListTotalCnt2(CDRParams cDRParams);
}
