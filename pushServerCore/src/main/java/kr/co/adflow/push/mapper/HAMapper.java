package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.HA;

/**
 * @author nadir93
 * @date 2014. 7. 24.
 */
public interface HAMapper {
	HA get() throws Exception;

	int put(HA ha) throws Exception;

	int post(HA ha) throws Exception;
}
