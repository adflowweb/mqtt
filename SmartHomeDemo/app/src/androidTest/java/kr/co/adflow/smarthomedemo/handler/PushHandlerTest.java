/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.smarthomedemo.handler;

import android.test.AndroidTestCase;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kr.co.adflow.push.client.mqttv3.Client;

/**
 * Created by nadir93 on 15. 10. 15..
 */
public class PushHandlerTest extends AndroidTestCase {

    Client client;

//    @BeforeClass
//    public static void oneTimeSetUp() {
//        // one-time initialization code
//        System.out.println("@BeforeClass - oneTimeSetUp");
//
//    }

//    @AfterClass
//    public static void oneTimeTearDown() {
//        // one-time cleanup code
//        System.out.println("@AfterClass - oneTimeTearDown");
//    }


    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Log.d("PushHandlerTest", "setUp시작()");
        //client = new Client(getContext());
        //client.keepAlive();
        Log.d("PushHandlerTest", "setUp종료()");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Log.d("PushHandlerTest", "tearDown시작()");
        //client.stop();
        Log.d("PushHandlerTest", "tearDown종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testContext() {
        assertTrue("The context should not be null", getContext() != null);
    }


}
