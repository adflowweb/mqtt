/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

// TODO: Auto-generated Javadoc
/**
 * The Interface TableMgtMapper.
 */
public interface TableMgtMapper {

	/**
	 * Select message.
	 *
	 * @param name the name
	 * @return the int
	 */
	int selectMessage(String name);

	/**
	 * Creates the message.
	 *
	 * @param name the name
	 * @return the int
	 */
	int createMessage(String name);

	/**
	 * Select content.
	 *
	 * @param name the name
	 * @return the int
	 */
	int selectContent(String name);

	/**
	 * Creates the content.
	 *
	 * @param name the name
	 * @return the int
	 */
	int createContent(String name);

	/**
	 * Select ack.
	 *
	 * @param name the name
	 * @return the int
	 */
	int selectAck(String name);

	/**
	 * Creates the ack.
	 *
	 * @param name the name
	 * @return the int
	 */
	int createAck(String name);

}
