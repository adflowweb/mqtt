package kr.co.adflow.pms.core.response;

import java.util.List;

import kr.co.adflow.pms.domain.AckData;

public class AckRes {

	private List<AckData> ackList;

	public List<AckData> getAckList() {
		return ackList;
	}

	public void setAckList(List<AckData> ackList) {
		this.ackList = ackList;
	}

	@Override
	public String toString() {
		return "AckList [ackList=" + ackList + "]";
	}

}
