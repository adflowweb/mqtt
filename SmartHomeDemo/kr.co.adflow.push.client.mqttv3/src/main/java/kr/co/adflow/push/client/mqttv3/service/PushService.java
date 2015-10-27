/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.service;

import java.util.Date;

/**
 * Created by nadir93 on 15. 10. 13..
 */
public interface PushService {

    /**
     * 토큰 등록 (IF_A_101)
     * <p>
     * 설명 : Mqtt 세션 인증 토큰을 등록한다
     * <p>
     * 제약사항 : 토큰 등록없이 MQTT Push는 동작하지 않습니다
     *
     * @param token
     * @return
     * @throws Exception
     */
    public String registerToken(String token, boolean rightAway);

    /**
     * 토큰 가져오기 (IF_A_102)
     * <p>
     * 설명 : mqtt 세션 인증 토큰을 가져온다
     *
     * @return
     */
    public String getToken();

    /**
     * startService 구현 대체
     * @return
     */
    //public String start();

    /**
     * stopService 구현 대체
     *
     * @return
     */
    //public String stop();

    /**
     * send ping (IF_A_105)
     * <p>
     * 설명 : tpc 채널 확인을 위해 실제 메시지를 보내본다 (QOS 1)
     *
     * @return
     */
    public String ping();

    /**
     * 메시지수신확인 (IF_A_107)
     * <p>
     * 설명 : 메시지 수신 확인 정보를 서버로 전송한다
     *
     * @return
     */
    public String ack(String msgId, Date ackTime);

    /**
     * 토픽 구독 (IF_A_108)
     * <p>
     * 설명 : 토픽을 구독한다
     *
     * @return
     */
    public String subscribe(String topic, int qos);

    /**
     * 토픽 구독해제 (IF_A_109)
     * <p>
     * 설명 : 토픽 구독을 해제 한다
     *
     * @return
     */
    public String unsubscribe(String topic);

    /**
     * 토픽 구독 정보 가져오기 (IF_A_110)
     * <p>
     * 설명 : 토픽 구독 정보를 가져온다
     *
     * @return
     */
    public String getSubscriptions();

    /**
     * 세션 상태 보기 (IF_A_111)
     * <p>
     * 설명 : 세션 상태 보기
     *
     * @return
     */
    public String getSession();

}
