package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
@JsonDeserialize(as = AuthResponseData.class)
abstract public class ResponseData {
}
