//
//  ADFPush.h
//  ADFPush
//
//  Created by gwangil on 2015. 9. 7..
//  Copyright (c) 2015년 kamy. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MqttOCClient.h"
#import <AVFoundation/AVFoundation.h>
#import "QueueFile.h"
#import "PushDataBase.h"



@interface ADFPush : NSObject {
    MqttClient *client;
}

@property (nonatomic, retain) MqttClient *client;
@property (nonatomic, retain) NSString *clientId;
@property bool loginMQTT;
@property PushDataBase *pushDataDB;

///[sk]
@property (nonatomic, retain) id Responder;
@property (nonatomic, strong) QueueFile *adfEnv;
@property (nonatomic, strong) QueueFile *adfTranLogQF;
@property (nonatomic, strong) QueueFile *adfErrorLogQF;
@property (nonatomic, retain) NSString *messageADF;
///[sk]


+ (id)sharedADFPush;
- (void)publish:(NSString *)topic payload:(NSString *)payload qos:(int)qos retained:(BOOL)retained;
- (void)subscribeMQTT:(NSString *)topicFilter qos:(int)qos;
- (void)unsubscribeMQTT:(NSString *)topicFilter;
- (void)disconnectMQTT:(int)timeout;
- (void)connectMQTT;
- (NSString *)callAck:(NSString *)msgId ackTime:(int)ackTime jobId:(int) jobId;
- (void)agentAck:(NSString *)msgId ackTime:(int)ackTime ackType:(NSString *) ackType;
- (void)addJobLog:(NSString *)jobName param1:(NSString *) param1 param2:(NSString *) param2 jsonYn:(BOOL)jsonYn data:(NSString *) data jogTypeError:(BOOL) jobTypeError;
- (NSString *)registerToken:(NSString *)token;
- (NSString *)getTokenMQTT;
- (NSString *)connectStateMQTT;
- (NSString *)cleanJobQueue;
- (void)getSubscriptions;
- (NSString *)registerADFPushEnv:(NSArray *)hosts ports:(NSArray *)ports cleanSesstion:(BOOL)cleanSesstion  token:(NSString *)token adfPushServerUrl:(NSString *)adfPushServerUrl;
- (NSString *)getAdfPushEnv;
- (void) callBackSelector:(NSString *)tempMethord data:(NSString *) data;
@end

