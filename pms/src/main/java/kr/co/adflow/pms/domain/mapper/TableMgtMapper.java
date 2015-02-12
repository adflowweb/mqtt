package kr.co.adflow.pms.domain.mapper;

public interface TableMgtMapper {

	int selectMessage(String name);
	int createMessage(String name);
	int selectContent(String name);
	int createContent(String name);
	int selectAck(String name);
	int createAck(String name);

}
