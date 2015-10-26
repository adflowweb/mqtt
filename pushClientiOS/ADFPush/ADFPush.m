//
//  ADFPush.m
//  ADFPush
//
//  Created by gwangil on 2015. 9. 7..
//  Copyright (c) 2015년 kamy. All rights reserved.
//

#import "ADFPush.h"
#import "JobBean.h"


@interface ADFPushEnv : NSObject

@property (nonatomic, retain) NSArray *hosts;
@property (nonatomic, retain) NSArray *posts;
@property (nonatomic, retain) NSString *token;
@property (nonatomic, retain) NSString *topic;
@property (nonatomic, retain) NSString *adfPushServerUrl;
@property int mqttKeepAliveInterval;

@end

@implementation ADFPushEnv

@synthesize hosts;
@synthesize posts;
@synthesize token;
@synthesize topic;
@synthesize adfPushServerUrl;
@synthesize mqttKeepAliveInterval;

@end



// Connect Callbacks
@interface ConnectCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage;
@end
@implementation ConnectCallbacks
- (void) onSuccess:(NSObject*) invocationContext
{
    [[ADFPush sharedADFPush] setLoginMQTT:true];
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"connectCallBack:";
    NSString *result = @"{\"status\": \"ok\",\"code\": 302200,\"message\": \"MQTT 서버에 접속이 되었습니다.\"}";
    
    SEL sel = NSSelectorFromString(tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'connectCallBack' not defind \n");
        return;
    }
}

NSData *dataForString(NSString *text)
{
    const char *s = [text UTF8String];
    return [NSData dataWithBytes:s length:strlen(s) + 1];
}

- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage
{
    NSLog(@"- invocationContext=%@  errorCode=%d  errorMessage=%@", invocationContext, errorCode, errorMessage);
    //    [[ADFPush sharedADFPush] addLogMessage:@"Failed to connect!" type:@"Action"];
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"connectCallBack:";
    
    NSString *result = nil;
    if (errorCode == 5) {
        result = @"{\"status\": \"fail\",\"code\": 302405,\"message\": \"MQTT 서버에 유요하지 않은 토큰으로 접속을 시도 했습니다.\"}";
    } else {
        result = @"{\"status\": \"fail\",\"code\": 302400,\"message\": \"MQTT 서버에 접속이 실패 되었습니다.\"}";
    }
    
    //    [aDFBean setMessage:@"Connected"];
    
    
    SEL sel = NSSelectorFromString(tempMethord);
    NSLog(@"Caller invoking method %@ \n", tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'connectCallBack' not defind \n");
        return;
    }
}
@end

// disConnect Callbacks
@interface DisConnectCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage;
@end
@implementation DisConnectCallbacks
- (void) onSuccess:(NSObject*) invocationContext
{
    //    NSLog(@"- invocationContext=%@", invocationContext);
    
    [[ADFPush sharedADFPush] setLoginMQTT:false];
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"disconnectCallBack:";
    
    //    [aDFBean setMessage:@"Connected"];
    NSString *result = @"{\"status\": \"ok\",\"code\": 303200,\"message\": \"MQTT 서버에 접속종료가 완료 되었습니다.\"}";
    
    SEL sel = NSSelectorFromString(tempMethord);
    NSLog(@"Caller invoking method %@ \n", tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'disconnectCallBack' not defind \n");
        return;
    }
    
    //    [[ADFPush sharedADFPush] addLogMessage:@"DisConnected to server!" type:@"Action"];
    
    //    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    //    [appDelegate updateConnectButton];
}
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage
{
    NSLog(@"- invocationContext=%@  errorCode=%d  errorMessage=%@", invocationContext, errorCode, errorMessage);
    //    [[ADFPush sharedADFPush] addLogMessage:@"Failed to disconnect!" type:@"Action"];
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"disconnectCallBack:";
    
    //    [aDFBean setMessage:@"Connected"];
    NSString *result = @"{\"status\": \"fail\",\"code\": 303400,\"message\": \"MQTT 서버에 접속종료가 실패되었습니다\"}";
    
    SEL sel = NSSelectorFromString(tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'disconnectCallBack' not defind \n");
        return;
    }
}
@end

// Publish Callbacks
@interface PublishCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString *)errorMessage;
@end
@implementation PublishCallbacks
- (void) onSuccess:(NSObject *) invocationContext
{
    NSLog(@"PublishCallbacks - onSuccess, invocationContext:%@", invocationContext);
    //    id tempResponder = [[ADFPush sharedADFPush] Responder];
    //    NSString *tempMethord = @"ackCallBack:";
    //
    //    //    [aDFBean setMessage:@"Connected"];
    //    NSString *result = @"{\"status\": \"ok\",\"code\": 309200,\"message\": \"수신확인 메세지 전달이 완료 되었습니다.\"}";
    //
    //    SEL sel = NSSelectorFromString(tempMethord);
    //    NSLog(@"Caller invoking method %@ \n", tempMethord);
    //
    //    if ([tempResponder respondsToSelector:sel]) {
    //        IMP imp = [tempResponder methodForSelector:sel];
    //        void (*func)(id, SEL, id) = (void *) imp;
    //        func(tempResponder, sel, result);
    //    } else {
    //        NSLog(@"[ADFPush] Warning : Method 'disconnectCallBack' not defind \n");
    //        return;
    //    }
    
}
- (void) onFailure:(NSObject *) invocationContext errorCode:(int) errorCode errorMessage:(NSString *)errorMessage
{
    NSLog(@"PublishCallbacks - onFailure");
}
@end

// ack Callbacks
@interface AckCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString *)errorMessage;
@end
@implementation AckCallbacks
- (void) onSuccess:(NSObject *) invocationContext
{
    NSLog(@"AckCallbacks - onSuccess, invocationContext:%@", invocationContext);
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"ackCallBack:";
    
    //    [aDFBean setMessage:@"Connected"];
    NSString *result = @"{\"status\": \"ok\",\"code\": 309200,\"message\": \"수신확인 메세지 전달이 완료 되었습니다.\"}";
    
    SEL sel = NSSelectorFromString(tempMethord);
    NSLog(@"Caller invoking method %@ \n", tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'AckCallbacks' not defind \n");
        return;
    }
    
}
- (void) onFailure:(NSObject *) invocationContext errorCode:(int) errorCode errorMessage:(NSString *)errorMessage
{
    NSLog(@"-AckCallbacks  invocationContext=%@  errorCode=%d  errorMessage=%@", invocationContext, errorCode, errorMessage);
    //    [[ADFPush sharedADFPush] addLogMessage:@"Failed to disconnect!" type:@"Action"];
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"ackCallBack:";
    
    //    [aDFBean setMessage:@"Connected"];
    NSString *result = @"{\"status\": \"fail\",\"code\": 309400,\"message\": \"수신확인 메세지 전달이 실패 했습니다\"}";
    
    SEL sel = NSSelectorFromString(tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'AckCallbacks' not defind \n");
        return;
    }
    
}
@end

// agent Ack Callbacks
@interface AgentAckCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString *)errorMessage;
@end
@implementation AgentAckCallbacks
- (void) onSuccess:(NSObject *) invocationContext
{
    
    NSLog(@"AgentAckCallbacks - onSuccess, invocationContext:%@", invocationContext);
    [[ADFPush sharedADFPush] setMessageADF:@"AckCallbackOK"];
    
}
- (void) onFailure:(NSObject *) invocationContext errorCode:(int) errorCode errorMessage:(NSString *)errorMessage
{
    NSLog(@"AgentAckCallbacks  invocationContext=%@  errorCode=%d  errorMessage=%@", invocationContext, errorCode, errorMessage);
    [[ADFPush sharedADFPush] setMessageADF:@"AckCallbackFail"];
}
@end


// Subscribe Callbacks
@interface SubscribeCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage;
@end
@implementation SubscribeCallbacks
- (void) onSuccess:(NSObject*) invocationContext
{
    //    NSLog(@"- invocationContext=%@", invocationContext);
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"subscribeCallBack:";
    
    NSMutableString * resultMu;
    resultMu = [[NSMutableString alloc]init];
    
    [resultMu appendFormat:@"{\"status\": \"ok\",\"code\": 305200,\"data\" : {\"topic\" : \"%@\"}, \"message\": \"구독신청이 완료되었습니다\"}",invocationContext];
    NSString * result = resultMu;
    
    
    
    SEL sel = NSSelectorFromString(tempMethord);
    NSLog(@"Caller invoking method %@ \n", tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'subscribeCallBack' not defind \n");
        return;
    }
    
}
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage
{
    NSLog(@"[ADFPush] SubscribeCallbacks - invocationContext=%@  errorCode=%d  errorMessage=%@", invocationContext, errorCode, errorMessage);
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"subscribeCallBack:";
    
    NSMutableString * resultMu;
    resultMu = [[NSMutableString alloc]init];
    
    [resultMu appendFormat:@"{\"status\": \"fail\",\"code\": 305400,\"data\" : {\"topic\" : \"%@\"}, \"message\": \"구독신청이 실패했습니다\"}",invocationContext];
    NSString * result = resultMu;
    
    
    
    SEL sel = NSSelectorFromString(tempMethord);
    NSLog(@"Caller invoking method %@ \n", tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'subscribeCallBack' not defind \n");
        return;
    }
}
@end

// Unsubscribe Callbacks
@interface UnsubscribeCallbacks : NSObject <InvocationComplete>
- (void) onSuccess:(NSObject*) invocationContext;
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage;
@end
@implementation UnsubscribeCallbacks
- (void) onSuccess:(NSObject*) invocationContext
{
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"unsubscribeCallBack:";
    
    NSMutableString * resultMu;
    resultMu = [[NSMutableString alloc]init];
    
    [resultMu appendFormat:@"{\"status\": \"ok\",\"code\": 306200,\"data\" : {\"topic\" : \"%@\"}, \"message\": \"구독해제가 완료되었습니다\"}",invocationContext];
    NSString * result = resultMu;
    
    
    
    SEL sel = NSSelectorFromString(tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'unsubscribeCallBack' not defind \n");
        return;
    }
}
- (void) onFailure:(NSObject*) invocationContext errorCode:(int) errorCode errorMessage:(NSString*) errorMessage
{
    NSLog(@"[ADFPush] UnsubscribeCallbacks - invocationContext=%@  errorCode=%d  errorMessage=%@", invocationContext, errorCode, errorMessage);
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"subscribeCallBack:";
    
    NSMutableString * resultMu;
    resultMu = [[NSMutableString alloc]init];
    
    [resultMu appendFormat:@"{\"status\": \"fail\",\"code\": 306400,\"data\" : {\"topic\" : \"%@\"}, \"message\": \"구독해제가 실패했습니다\"}",invocationContext];
    NSString * result = resultMu;
    
    
    
    SEL sel = NSSelectorFromString(tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'unsubscribeCallBack' not defind \n");
        return;
    }
}




@end

@interface GeneralCallbacks : NSObject <MqttCallbacks>
- (void) onConnectionLost:(NSObject*)invocationContext errorMessage:(NSString*)errorMessage;
- (void) onMessageArrived:(NSObject*)invocationContext message:(MqttMessage*)msg;
- (void) onMessageDelivered:(NSObject*)invocationContext messageId:(int)msgId;
@end
@implementation GeneralCallbacks
- (void) onConnectionLost:(NSObject*)invocationContext errorMessage:(NSString*)errorMessage
{
    //    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    //    [appDelegate updateConnectButton];
    //    [appDelegate reloadSubscriptionList];
    
    NSLog(@"- invocationContext=%@  errorMessage=%@", invocationContext, errorMessage);
    //    [[ADFPush sharedADFPush] addLogMessage:@"Failed to disconnect!" type:@"Action"];
    
    id tempResponder = [[ADFPush sharedADFPush] Responder];
    NSString *tempMethord = @"connectLostCallBack:";
    
    //    [aDFBean setMessage:@"Connected"];
    NSString *result = @"{\"status\": \"fail\",\"code\": 302401,\"message\": \"MQTT 연결이 끊어졌습니다\"}";
    
    SEL sel = NSSelectorFromString(tempMethord);
    
    if ([tempResponder respondsToSelector:sel]) {
        IMP imp = [tempResponder methodForSelector:sel];
        void (*func)(id, SEL, id) = (void *) imp;
        func(tempResponder, sel, result);
    } else {
        NSLog(@"[ADFPush] Warning : Method 'connectLostCallBack' not defind \n");
        return;
    }
}
- (void) onMessageArrived:(NSObject*)invocationContext message:(MqttMessage*)msg
{
    int qos = msg.qos;
    BOOL retained = msg.retained;
    //    NSString *payload = [[NSString alloc] initWithBytes:msg.payload length:msg.payloadLength encoding:NSASCIIStringEncoding];
    NSString *payload = [[NSString alloc] initWithBytes:msg.payload length:msg.payloadLength encoding:NSUTF8StringEncoding];
    //    NSString* aString = [[[NSString alloc] initWithString:payload encoding:0×80000840] autorelease];
    
    NSString *topic = msg.destinationName;
    NSString *retainedStr = retained ? @" [retained]" : @"";
    NSString *logStr = [NSString stringWithFormat:@"[%@ QoS:%d] %@%@", topic, qos, payload, retainedStr];
    NSLog(@"- %@", logStr);
    NSLog(@"GeneralCallbacks - onMessageArrived!");
    
    PushDataBase *pushDataDB = [[ADFPush sharedADFPush] pushDataDB];
    JobBean *job;
    JobBean *job2;
    
    NSData *jData = [payload dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:jData options:NSJSONReadingMutableContainers error:nil];
    
    
    id tempResponder;
    NSString *tempMethord;
    SEL sel;
    IMP imp;
    NSString *result;
    //    NSDate *todate;
    //    NSString *sDate;
    //    NSDateFormatter *dataFormatter;
    int timestamp;
    
    int msgType = [json[@"msgType"] intValue];
    int jobId;
    
    switch (msgType) {
        case 100:
            
            //일반 메세지 DB 저장
            job = [[JobBean alloc]init];
            
            timestamp = [[NSDate date] timeIntervalSince1970];
            //            todate = [NSDate date];
            //            dataFormatter = [[NSDateFormatter alloc] init];
            //            [dataFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
            //            sDate = [dataFormatter stringFromDate: todate];
            
            [job setMsgType:[json[@"msgType"] intValue]];
            [job setAck:[json[@"ack"] intValue]];
            [job setQos:qos];
            [job setContent:json[@"content"]];
            [job setMsgId:json[@"msgId"]];
            [job setContentType:json[@"contentType"]];
            [job setTopic:topic];
            [job setServiceId:json[@"serviceId"]];
            [job setIssueTime:timestamp];
            
            
            jobId = [pushDataDB insertJob:job];
            
            if (jobId == -1) {
                NSLog(@"[ADFPush] onMessageArrived - ERROR : insert Job Error \n");
                return;
            }
            
            // Agent Ack send
            if (job.ack > 0) {
                // job Table - agent ack insert
                job2 = [[JobBean alloc]init];
                [job2 setMsgType:300];
                [job2 setAck:[json[@"ack"] intValue]];
                [job2 setQos:qos];
                [job2 setContent:@""];
                [job2 setMsgId:json[@"msgId"]];
                [job2 setContentType:json[@"contentType"]];
                [job2 setTopic:topic];
                [job2 setServiceId:json[@"serviceId"]];
                [job2 setIssueTime:timestamp];
                
                [pushDataDB insertJob:job2];
            }
            
            
            // onMessageArrivedCallBack Call
            tempResponder = [[ADFPush sharedADFPush] Responder];
            tempMethord = @"onMessageArrivedCallBack:";
            
            sel = NSSelectorFromString(tempMethord);
            NSLog(@"Caller invoking method %@ \n", tempMethord);
            //    [tempResponder performSelector: sel withObject: logStr];
            
            if ([tempResponder respondsToSelector:sel]) {
                //        NSLog(@" SEL OK \n");
                imp = [tempResponder methodForSelector:sel];
                void (*func)(id, SEL, id) = (void *) imp;
                
                result = [NSString stringWithFormat:@"{\"content\": \"%@\",\"msgId\":\"%@\",\"contentType\":\"%@\",\"topic\":\"%@\",\"qos\":%d,\"jobId\":%d}",job.content,job.msgId, job.contentType, job.topic, job.qos, jobId];
                
                func(tempResponder, sel, result);
            } else {
                NSLog(@"[ADFPush] Warning : Method 'onMessageArrivedCallBack' not defind \n");
                return;
            }
            
            break;
            
        case 200:
            
            //mqttKeepAliveInterval setting
            job = [[JobBean alloc]init];
            
            timestamp = [[NSDate date] timeIntervalSince1970];
            [job setMsgType:[json[@"msgType"] intValue]];
            [job setAck:[json[@"ack"] intValue]];
            [job setQos:qos];
            [job setContent:json[@"content"]];
            [job setMsgId:json[@"msgId"]];
            [job setContentType:json[@"contentType"]];
            [job setTopic:topic];
            [job setServiceId:json[@"serviceId"]];
            [job setIssueTime:timestamp];
            
            [pushDataDB insertJob:job];
            
            break;
            
        default:
            [[ADFPush sharedADFPush] addJobLog:@"JobError" param1:payload param2:@"" param3:@""];
            break;
    }
    
}


- (void) onMessageDelivered:(NSObject*)invocationContext messageId:(int)msgId
{
    NSLog(@"GeneralCallbacks - onMessageDelivered!, messageID:%d", msgId);
}


- (NSDictionary *)jSonToObject:(NSString *)jSonData
{
    
    NSData *jData = [jSonData dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:jData options:NSJSONReadingMutableContainers error:nil];
    
    return json;
}
- (NSString *)objectToJSon:(NSDictionary *)jObjectData
{
    
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jObjectData options:0 error:nil];
    
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}
@end





////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
//////////////////   ADFPUSH   /////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////


@implementation ADFPush
@synthesize client;

static float JOBINTERVAL = 60.0f;
NSArray *MQTTHOSTS;
NSArray *MQTTPORTS;
NSString *MQTTTOKEN;
NSString *ADFPUSHHOST;
int MQTTKEEPALIVEINTERVAL;


#pragma mark Singleton Methods

+ (id)sharedADFPush {
    static ADFPush *shared = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        shared = [[self alloc] init];
    });
    return shared;
}

- (id)init {
    if (self = [super init]) {
        self.client = [MqttClient alloc];
        self.clientId = nil;
        self.loginMQTT = nil;
        self.Responder = nil;
        self.client.callbacks = [[GeneralCallbacks alloc] init];
        self.pushDataDB = [[PushDataBase alloc]init];
        [self.pushDataDB initWithDataBase];
        
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
                                                             NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        NSString *filePath =
        [documentsDirectory stringByAppendingPathComponent:@"adfEvn.q"];
        self.adfEnv = [QueueFile queueFileWithPath:filePath];
        
        
        if ([self.adfEnv size] > 0) {
            NSString *adfEnvJson = [NSString stringWithUTF8String:[[self.adfEnv peek] bytes]];
            NSData *jData = [adfEnvJson dataUsingEncoding:NSUTF8StringEncoding];
            NSDictionary *json = [NSJSONSerialization JSONObjectWithData:jData options:NSJSONReadingMutableContainers error:nil];
            
            MQTTHOSTS = json[@"hosts"];
            MQTTPORTS = json[@"ports"];
            MQTTTOKEN = json[@"token"];
            ADFPUSHHOST = json[@"adfPushServerUrl"];
            MQTTKEEPALIVEINTERVAL = [json[@"mqttKeepAliveInterval"] intValue];
        } else {
            MQTTHOSTS = nil;
            MQTTPORTS = nil;
            MQTTTOKEN = nil;
            ADFPUSHHOST = nil;
            MQTTKEEPALIVEINTERVAL = 30;
        }
        
        
        filePath = [documentsDirectory stringByAppendingPathComponent:@"jobLog.q"];
        self.jobLogQF = [QueueFile queueFileWithPath:filePath];
        self.messageADF = nil;
        
        
        
        //Job Background loop run - start
        [NSTimer scheduledTimerWithTimeInterval:JOBINTERVAL target:self selector:@selector(jobAgent) userInfo:nil repeats:YES];
        //Job Background loop run - start
        
        NSLog(@"============   Background start");
        
    }
    return self;
}


- (void)connectMQTT:(BOOL)cleanSession
{
    @try {
        // Job Logging
        if (MQTTHOSTS.count > 0 && MQTTPORTS.count >0) {
            [[ADFPush sharedADFPush] addJobLog:@"connectMQTT" param1:MQTTHOSTS[0] param2:MQTTPORTS[0] param3:(cleanSession)? @"true" : @"false"];
        }
        
        
        //    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
        //                                                         NSUserDomainMask, YES);
        //    NSString *documentsDirectory = [paths objectAtIndex:0];
        //    NSString *filePath =
        //    [documentsDirectory stringByAppendingPathComponent:@"token.q"];
        //    QueueFile  * tokenQ = [QueueFile queueFileWithPath:filePath];
        //
        //    NSLog(@"====== test size :: %d", [tokenQ size]);
        
        // token Queue read
        //    NSString *clientId = [NSString stringWithUTF8String:[[tokenQ peek] bytes]];
        
        //    NSString * clientId = [[ADFPush sharedADFPush] getTokenMQTT];
        NSLog(@"====== MQTTTOKEN :: %@", MQTTTOKEN);
        client = [client initWithHosts:MQTTHOSTS ports:MQTTPORTS clientId:MQTTTOKEN];
        ConnectOptions *opts = [[ConnectOptions alloc] init];
        opts.timeout = 3600;
        opts.cleanSession = cleanSession;
        opts.keepAliveInterval = 30;
        
        SSLOptions *ssloti = [[SSLOptions alloc] init];
        ssloti.enableServerCertAuth = FALSE;
        opts.sslProperties = ssloti;
        
        
        NSLog(@"host=%@, port=%@, clientId=%@, cleanSession=%@", MQTTHOSTS, MQTTPORTS, MQTTTOKEN,  (cleanSession)? @"true" : @"false");
        [client connectWithOptions:opts invocationContext:self onCompletion:[[ConnectCallbacks alloc] init]];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] connectMQTT - NSException: %@", exception);
    }
    
}



- (void)disconnectMQTT:(int)timeout {
    
    @try {
        // Job Logging
        [[ADFPush sharedADFPush] addJobLog:@"disconnectMQTT" param1:[NSString stringWithFormat: @"%d", timeout]  param2:@"" param3:@""];
        
        DisconnectOptions *opts = [[DisconnectOptions alloc] init];
        [opts setTimeout:timeout];
        
        [client disconnectWithOptions:opts invocationContext:self onCompletion:[[DisConnectCallbacks alloc] init]];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] disconnectMQTT - NSException: %@", exception);
    }
    
    
    
}

- (void)publish:(NSString *)topic payload:(NSString *)payload qos:(int)qos retained:(BOOL)retained
{
    @try {
        // Job Logging
        [[ADFPush sharedADFPush] addJobLog:@"publish" param1:topic  param2:[NSString stringWithFormat: @"%d", qos] param3:(retained)? @"true" : @"false"];
        
        NSLog(@"=========== playload1 :%@", payload);
        
        NSString *retainedStr = retained ? @" [retained]" : @"";
        NSString *logStr = [NSString stringWithFormat:@"[%@] %@%@", topic, payload, retainedStr];
        NSLog(@"- %@", logStr);
        //    [[ADFPush sharedADFPush] addLogMessage:logStr type:@"Publish"];
        
        NSLog(@"=========== playload2 :%@", payload);
        MqttMessage *msg = [[MqttMessage alloc] initWithMqttMessage:topic payload:(char*)[payload UTF8String] length:(int)payload.length qos:qos retained:retained duplicate:NO];
        NSLog(@"=========== msg :%@", msg);
        
        [client send:msg invocationContext:self onCompletion:[[PublishCallbacks alloc] init]];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] publish - NSException: %@", exception);
    }
    
    
    
    
}

- (void)subscribeMQTT:(NSString *)topicFilter qos:(int)qos
{
    @try {
        // Job Logging
        [[ADFPush sharedADFPush] addJobLog:@"subscribeMQTT" param1:topicFilter param2:[NSString stringWithFormat: @"%d", qos]   param3:@""];
        
        NSLog(@"topicFilter=%@, qos=%d", topicFilter, qos);
        NSLog(@"=====  subscribe start");
        [client subscribe:topicFilter qos:qos invocationContext:topicFilter onCompletion:[[SubscribeCallbacks alloc] init]];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] subscribeMQTT - NSException: %@", exception);
    }
    
    
    
    
}

- (void)unsubscribeMQTT:(NSString *)topicFilter
{
    @try {
        // Job Logging
        [[ADFPush sharedADFPush] addJobLog:@"unsubscribeMQTT" param1:topicFilter  param2:@"" param3:@""];
        
        NSLog(@"topicFilter=%@", topicFilter);
        [client unsubscribe:topicFilter invocationContext:topicFilter onCompletion:[[UnsubscribeCallbacks alloc] init]];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] unsubscribeMQTT - NSException: %@", exception);
    }
    
    
}

- (NSString *)registerToken:(NSString *)token{
    
    // Job Logging
    [[ADFPush sharedADFPush] addJobLog:@"unsubscribeMQTT" param1:token  param2:@"" param3:@""];
    
    NSString * result = nil;
    
    if (token.length < 24) {
        
        @try {
            //            NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
            //                                                                 NSUserDomainMask, YES);
            //            NSString *documentsDirectory = [paths objectAtIndex:0];
            //            NSString *filePath =
            //            [documentsDirectory stringByAppendingPathComponent:@"token.q"];
            //            QueueFile  * tokenQ = [QueueFile queueFileWithPath:filePath];
            //
            //            [tokenQ clear];
            //            // token Queue insert
            //            [tokenQ add:dataForString(token)];
            
            QueueFile * adfEnv = [ [ADFPush sharedADFPush] adfEnv];
            NSString *envJson;
            if ([adfEnv size] > 0) {
                // token Queue read
                NSString *adfEnvJson = [NSString stringWithUTF8String:[[self.adfEnv peek] bytes]];
                
                NSData *jData = [adfEnvJson dataUsingEncoding:NSUTF8StringEncoding];
                NSMutableDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:jData options:NSJSONReadingMutableContainers error:nil];
                
                [jsonDic removeObjectForKey:@"token"];
                [jsonDic setObject:token forKey:@"token"];
                
                NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDic options:NSJSONWritingPrettyPrinted error:nil];
                envJson = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                
            }else {
                NSArray * temp = [[NSArray alloc] init];
                NSNumber *mqttKeepAliveInterval = [[NSNumber alloc] initWithInt:MQTTKEEPALIVEINTERVAL];
                NSDictionary *dict = [[NSDictionary alloc] initWithObjectsAndKeys:
                                      temp,@"hosts",
                                      temp,@"ports",
                                      token,@"token",
                                      @"",@"adfPushServerUrl",
                                      mqttKeepAliveInterval,@"mqttKeepAliveInterval",
                                      nil];
                
                NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:NSJSONWritingPrettyPrinted error:nil];
                envJson = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                
                
            }
            NSLog(@"envJson : %@", envJson);
            
            [adfEnv clear];
            [adfEnv add:dataForString(envJson)];
            MQTTTOKEN = token;
            
            result = @"{\"status\": \"ok\",\"code\": 301200,\"message\": \"토큰등록이 완료되었습니다\"}";
            
            
            //연결이 되어 있을때, 연결 종료.
            MqttClient *mClient = [[ADFPush sharedADFPush] client];
            if ([mClient isConnected]) {
                [[ADFPush sharedADFPush] disconnectMQTT:2];
            }
            
        }
        @catch (NSException *exception) {
            NSLog(@"[ADFError] NSException: %@", exception);
            result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 301500,\"message\": \"%@\"}",exception];
        }
        @finally {
            
        }
    } else {
        
        NSLog(@"[ADFError] token length error - token: %@", token);
        result = @"{\"status\": \"fail\",\"code\": 301401,\"message\": \"토큰길이가 큽니다.\"}";
    }
    
    
    
    return result;
}

- (NSString *)getTokenMQTT{
    
    // Job Logging
    [[ADFPush sharedADFPush] addJobLog:@"getTokenMQTT" param1:@""  param2:@"" param3:@""];
    
    NSString * result;
    
    @try {
        //        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
        //                                                             NSUserDomainMask, YES);
        //        NSString *documentsDirectory = [paths objectAtIndex:0];
        //        NSString *filePath =
        //        [documentsDirectory stringByAppendingPathComponent:@"token.q"];
        //        QueueFile  * tokenQ = [QueueFile queueFileWithPath:filePath];
        //
        //        // token Queue read
        //        NSString *token = [NSString stringWithUTF8String:[[tokenQ peek] bytes]];
        result = [NSString stringWithFormat:@"{\"status\": \"ok\",\"data\":{ \"token\":\"%@\"},\"code\": 310200,\"message\": \"토큰 가져오기가  완료 되었습니다\"}",MQTTTOKEN];
        
        
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 301500,\"message\": \"%@\"}",exception];
    }
    @finally {
        
    }
    
    
    return result;
}

- (NSString *)connectStateMQTT{
    
    // Job Logging
    [[ADFPush sharedADFPush] addJobLog:@"connectStateMQTT" param1:@""  param2:@"" param3:@""];
    
    NSString * result;
    
    @try {
        MqttClient *mClient = [[ADFPush sharedADFPush] client];
        
        
        if ([mClient isConnected]) {
            result = @"{\"status\": \"ok\",\"code\": 304200,\"message\": \"MQTT 서버에 연결된 상태입니다.\"}";
        } else {
            result = @"{\"status\": \"fail\",\"code\": 304400,\"message\": \"MQTT 서버에 연결되어 있지 않습니다.\"}";
        }
        
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 304500,\"message\": \"%@\"}",exception];
    }
    @finally {
        
    }
    return result;
}

- (NSString *)cleanJobQueue{
    // Job Logging
    [[ADFPush sharedADFPush] addJobLog:@"cleanJobQueue" param1:@""  param2:@"" param3:@""];
    
    NSString * result;
    
    
    PushDataBase *pushDataDB = [[ADFPush sharedADFPush] pushDataDB];
    NSString * sqlResult = [pushDataDB deleteJobAll]; //Job 삭제
    
    
    if ([sqlResult isEqualToString:@"OK"]) {
        result = @"{\"status\": \"ok\",\"code\": 308200,\"message\": \"작업큐 초기화가 완료되었습니다\"}";
    } else {
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 308500,\"message\": \"%@\"}",sqlResult];
    }
    
    return result;
}

- (void) addJobLog:(NSString *)jobName param1:(NSString *) param1 param2:(NSString *) param2 param3:(NSString *) param3{
    
    @try {
        NSDate *now = [NSDate date]; //현재 날짜로 객체 생성
        NSDateFormatter *dateFormat=[[NSDateFormatter alloc] init];
        [dateFormat setDateFormat:@"yyyy-MM-dd hh:mm:ss:SS"];
        NSString *dateString = [dateFormat stringFromDate:now];
        
        NSString *result = [NSString stringWithFormat:@"{\"date\": \"%@\",\"jobName\": \"%@=%@=%@=%@\"}",dateString, jobName,param1,param2,param3];
        
        QueueFile * jobLogQF = [ [ADFPush sharedADFPush] jobLogQF];
        if ([jobLogQF size] > 3000) {
            [jobLogQF remove];
        }
        [jobLogQF add:dataForString(result)];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
    }
}

- (NSString *)getSubscriptions{
    // Job Logging
    [[ADFPush sharedADFPush] addJobLog:@"getTokenMQTT" param1:@""  param2:@"" param3:@""];
    
    NSString * result;
    
    //    NSURL *url = [[NSURL alloc] initWithString:@"https://14.63.217.141:8081/v1/pms/adm/cmm/auth"];
    //    NSURL *url = [[NSURL alloc] initWithString:@"http://127.0.0.1:8080/v1/pms/adm/cmm/auth"];
    //    NSURL *url = [[NSURL alloc] initWithString:@"https://easy-message.co.kr/v1/pms/adm/cmm/auth"];
    
    NSString *host = @"http://112.223.76.75:18080";
    
    @try {
        //        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
        //                                                             NSUserDomainMask, YES);
        //        NSString *documentsDirectory = [paths objectAtIndex:0];
        //        NSString *filePath =
        //        [documentsDirectory stringByAppendingPathComponent:@"token.q"];
        //        QueueFile  * tokenQ = [QueueFile queueFileWithPath:filePath];
        //
        //        // token Queue read
        //        NSString *token = [NSString stringWithUTF8String:[[tokenQ peek] bytes]];
        
        NSString *urlString = [NSString stringWithFormat:@"%@/v1/token/subscriptions/%@",host,MQTTTOKEN];
        
        NSLog(@"urlMu :%@", urlString);
        
        NSURL *url = [[NSURL alloc] initWithString:urlString];
        
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        
        //    [NSURLRequest setAllowsAnyHTTPSCertificate:YES forHost:[url host]];
        
        [request setURL:url];
        [request setCachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData];
        [request setTimeoutInterval:30.0];
        [request setHTTPMethod:@"GET"];
        [request addValue:MQTTTOKEN forHTTPHeaderField:@"X-Application-Key"];
        
        //    [NSURLConnection connectionWithRequest:request delegate:self];
        NSHTTPURLResponse * response;
        //        NSError * error = nil;
        //
        //        NSData * data = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        //
        //        if (error != nil) {
        //            NSLog(@"[ADFPush] Error on load = %@", [error localizedDescription]);
        //
        //            result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 307500,\"message\": \"%@\"}",[error localizedDescription]];
        //
        //            return result;
        //        }
        
        dispatch_semaphore_t    sem;
        __block NSData * result3;
        __block NSString * result4;
        
        result3 = nil;
        result4 = nil;
        sem = dispatch_semaphore_create(0);
        
        //ios7 이상만 사용 가능
        [[[NSURLSession sharedSession] dataTaskWithRequest:request
                                         completionHandler:^(NSData *data, NSURLResponse *response, NSError *error2) {
                                             if (error2 != nil) {
                                                 NSLog(@"[ADFPush] Error on load = %@", [error2 localizedDescription]);
                                                 result4 = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 307500,\"message\": \"%@\"}",[error2 localizedDescription]];
                                                 
                                             }
                                             if (error2 == nil) {
                                                 result3 = data;
                                             }
                                             dispatch_semaphore_signal(sem);
                                         }] resume];
        
        dispatch_semaphore_wait(sem, 5); //timeout 5 sec
        
        if (result4 != nil) {
            return result4;
        }
        
        //HTTP 상태를 검사한다.
        if ([response isKindOfClass:[NSHTTPURLResponse class]]) {
            //        NSHTTPURLResponse * httpResponse = (NSHTTPURLResponse *) response;
            
            if ([response statusCode] != 200) {
                NSLog(@"[ADFPush] httpResponse statusCode  = %ld", (long)[response statusCode]);
                
                result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 307500,\"message\": \"httpResponse statusCode  = %ld\"}",(long)[response statusCode]];
                return result;
            }
        }
        
        //     NSLog(@"[ADFPush] httpResponse statusCode  = %ld", (long)[response statusCode]);
        
        NSString *content = [[NSString alloc] initWithData:result3 encoding:NSUTF8StringEncoding];
        
        NSLog(@" content : %@",content);
        result = content;
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 307500,\"message\": \"%@\"}",exception];
    }
    @finally {
        
    }
    
    return result;
}


- (NSString *)callAck:(NSString *)msgId ackTime:(int)ackTime jobId:(int) jobId{
    
    NSLog(@"callAck - start");
    NSString *result;
    
    @try {
        PushDataBase *pushDataDB = [[ADFPush sharedADFPush] pushDataDB];
        int ackValue = [pushDataDB getAck:jobId]; //0=ackNo, 1=ackOK, 2=DB not found
        //        NSDateFormatter *dataFormatter;
        //
        //        dataFormatter = [[NSDateFormatter alloc] init];
        //        [dataFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        //
        //        NSString *sDate = [dataFormatter stringFromDate: ackTime];
        // Job Logging
        [[ADFPush sharedADFPush] addJobLog:@"callAck" param1:msgId  param2:[NSString stringWithFormat: @"%d",ackTime] param3:[NSString stringWithFormat: @"%d", ackValue] ];
        
        if (ackValue == 1) {
            JobBean *job = [[JobBean alloc]init];
            
            [job setMsgType:301];
            [job setAck:0];
            [job setQos:0];
            [job setContent:@""];
            [job setMsgId:msgId];
            [job setContentType:@""];
            [job setTopic:@""];
            [job setServiceId:@""];
            [job setIssueTime:ackTime];
            
            [pushDataDB insertJob:job];
            
            PushDataBase *pushDataDB = [[ADFPush sharedADFPush] pushDataDB];
            [pushDataDB deleteJobId:jobId]; //Job 삭제
        } else if (ackValue == 0) {
            PushDataBase *pushDataDB = [[ADFPush sharedADFPush] pushDataDB];
            [pushDataDB deleteJobId:jobId]; //Job 삭제
        }
        result = @"{\"status\": \"ok\",\"code\": 309200,\"message\": \"수신확인 메세지가 작업 DB에 저장이 되었습니다.\"}";
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 309500,\"message\": \"%@\"}",exception];
    }
    @finally {
        
    }
    
    return result;
}



- (void)agentAck:(NSString *)msgId ackTime:(int)ackTime ackType:(NSString *) ackType {
    
    NSLog(@"agentAck - start");
    
    @try {
        
        
        // Job Logging
        [[ADFPush sharedADFPush] addJobLog:@"agentAck" param1:msgId  param2:[NSString stringWithFormat: @"%d",ackTime] param3:ackType];
        
        
        //    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
        //                                                         NSUserDomainMask, YES);
        //    NSString *documentsDirectory = [paths objectAtIndex:0];
        //    NSString *filePath =
        //    [documentsDirectory stringByAppendingPathComponent:@"token.q"];
        //    QueueFile  * tokenQ = [QueueFile queueFileWithPath:filePath];
        //
        //    // token Queue read
        //    NSString *token = [NSString stringWithUTF8String:[[tokenQ peek] bytes]];
        
        long  currentTimeMillis = (long long) ackTime*1000;
        
        NSString *payload = [NSString stringWithFormat:@"{\"msgId\": \"%@\",\"ackTime\": %ld,\"token\": \"%@\",\"ackType\": \"%@\"}",msgId,currentTimeMillis,MQTTTOKEN,ackType];
        
        NSLog(@"=========== playload :%@", payload);
        NSString *topic = @"adfpush/ack";
        
        
        MqttMessage *msg = [[MqttMessage alloc] initWithMqttMessage:topic payload:(char*)[payload UTF8String] length:(int)payload.length qos:1 retained:false duplicate:NO];
        
        //    NSLog(@"=========== msg :%@", msg);
        
        [client send:msg invocationContext:self onCompletion:[[AgentAckCallbacks alloc] init]];
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] agentAck - NSException: %@", exception);
    }
    
    
    
    
}




-(void)jobAgent {
    
    PushDataBase *pushDataDB = [[ADFPush sharedADFPush] pushDataDB];
    NSArray *jobList = [pushDataDB getJobList];
    JobBean *job = [[JobBean alloc]init];
    
    
    // 연결체크 후 연결이 안되어 있으면 다시 연결 시도.
    MqttClient *mClient = [[ADFPush sharedADFPush] client];
    if (![mClient isConnected] && [[ADFPush sharedADFPush] loginMQTT]) {
        [[ADFPush sharedADFPush] connectMQTT:false];
    }
    
    
    id tempResponder;
    NSString *tempMethord;
    SEL sel;
    IMP imp;
    NSString * result;
    
    NSString *queMsg;
    QueueFile * adfEnv;
    NSString *envJson;
    NSNumber *mqttKeepAliveInterval;
    NSArray * temp;
    NSData *jData;
    NSData *jsonData;
    NSMutableDictionary *jsonDic;
    NSDictionary *dict;
    NSString *adfEnvJson;
    
    for (int i=0; i < jobList.count; i++) {
        job = [jobList objectAtIndex:i];
        switch (job.msgType) {
            case 100:
                tempResponder = [[ADFPush sharedADFPush] Responder];
                tempMethord = @"onMessageArrivedCallBack:";
                
                sel = NSSelectorFromString(tempMethord);
                NSLog(@"Caller invoking method %@ \n", tempMethord);
                //    [tempResponder performSelector: sel withObject: logStr];
                
                if ([tempResponder respondsToSelector:sel]) {
                    //        NSLog(@" SEL OK \n");
                    imp = [tempResponder methodForSelector:sel];
                    void (*func)(id, SEL, id) = (void *) imp;
                    
                    result = [NSString stringWithFormat:@"{\"content\": \"%@\",\"msgId\":\"%@\",\"contentType\":\"%@\",\"topic\":\"%@\",\"qos\":%d,\"jobId\":%d}",job.content,job.msgId, job.contentType, job.topic, job.qos, job.jobId];
                    
                    func(tempResponder, sel, result);
                    
                } else {
                    NSLog(@"[ADFPush] Warning : Method 'onMessageArrivedCallBack' not defind \n");
                }
                
                break;
                
            case 200:
                
                adfEnv = [ [ADFPush sharedADFPush] adfEnv];
                
                adfEnvJson = [NSString stringWithUTF8String:[[self.adfEnv peek] bytes]];
                
                jData = [adfEnvJson dataUsingEncoding:NSUTF8StringEncoding];
                jsonDic = [NSJSONSerialization JSONObjectWithData:jData options:NSJSONReadingMutableContainers error:nil];
                mqttKeepAliveInterval = [[NSNumber alloc] initWithInt:[jsonDic[@"content"][@"mqttKeepAliveInterval"] intValue]];
                
                if ([adfEnv size] > 0) {
                    [jsonDic removeObjectForKey:@"mqttKeepAliveInterval"];
                    [jsonDic setObject:mqttKeepAliveInterval forKey:@"mqttKeepAliveInterval"];
                    
                    jsonData = [NSJSONSerialization dataWithJSONObject:jsonDic options:NSJSONWritingPrettyPrinted error:nil];
                    envJson = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                    
                }else {
                    temp = [[NSArray alloc] init];
                    dict = [[NSDictionary alloc] initWithObjectsAndKeys:
                            temp,@"hosts",
                            temp,@"ports",
                            @"",@"token",
                            @"",@"adfPushServerUrl",
                            mqttKeepAliveInterval,@"mqttKeepAliveInterval",
                            nil];
                    
                    jsonData = [NSJSONSerialization dataWithJSONObject:dict options:NSJSONWritingPrettyPrinted error:nil];
                    envJson = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                    
                    
                }
                NSLog(@"envJson : %@", envJson);
                
                [adfEnv clear];
                [adfEnv add:dataForString(envJson)];
                MQTTKEEPALIVEINTERVAL = [jsonDic[@"data"][@"mqttKeepAliveInterval"] intValue];
                
                result = @"{\"status\": \"ok\",\"code\": 301200,\"message\": \"토큰등록이 완료되었습니다\"}";
                
                
                //연결이 되어 있을때, 연결 종료.
                if ([mClient isConnected]) {
                    // 토큰 변경이 없기 때문에 연결을 하면 자동으로 연결이 로스트 되고 다시 연결이 됨.
                    [[ADFPush sharedADFPush] connectMQTT:false];
                }
                
                break;
                
                
            case 300:
                if ([mClient isConnected]) {
                    [self agentAck:job.msgId ackTime:job.issueTime ackType:@"agent"];
                    [pushDataDB deleteJobId:job.jobId];
                }
                
                
                break;
                
            case 301:
                if ([mClient isConnected]) {
                    [self agentAck:job.msgId ackTime:job.issueTime ackType:@"app"];
                    [pushDataDB deleteJobId:job.jobId];
                }
                break;
                
                
            default:
                
                [self addJobLog:@"JobError" param1:queMsg param2:@"" param3:@""];
                NSLog(@"[ADFPush] jobAgent msgType error  : %@", queMsg);
                [pushDataDB deleteJobId:job.jobId];
                break;
        }
    }
}

- (NSString *)registerADFPushEnv:(NSArray *)hosts ports:(NSArray *)ports token:(NSString *)token adfPushServerUrl:(NSString *)adfPushServerUrl{
    NSString *result;
    
    NSNumber *mqttKeepAliveInterval = [[NSNumber alloc] initWithInt:MQTTKEEPALIVEINTERVAL]; //defult 30 sec
    
    @try {
        
        NSDictionary *dict = [[NSDictionary alloc] initWithObjectsAndKeys:
                              hosts,@"hosts",
                              ports,@"ports",
                              token,@"token",
                              adfPushServerUrl,@"adfPushServerUrl",
                              mqttKeepAliveInterval,@"mqttKeepAliveInterval",
                              nil];
        
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:NSJSONWritingPrettyPrinted error:nil];
        NSString *envJson = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        
        NSLog(@"envJson : %@", envJson);
        
        
        QueueFile * adfEnv = [ [ADFPush sharedADFPush] adfEnv];
        [adfEnv clear];
        [adfEnv add:dataForString(envJson)];
        MQTTHOSTS = hosts;
        MQTTPORTS = ports;
        MQTTTOKEN = token;
        ADFPUSHHOST = adfPushServerUrl;
        //        MQTTKEEPALIVEINTERVAL = 30;
        
        result = @"{\"status\": \"ok\",\"code\": 312200,\"message\": \"ADFPUSH 환경이 설정되었습니다.\"}";
        
        //연결이 되어 있을때, 연결 종료.
        MqttClient *mClient = [[ADFPush sharedADFPush] client];
        if ([mClient isConnected]) {
            [[ADFPush sharedADFPush] disconnectMQTT:2];
        }
        
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 312500,\"message\": \"%@\"}",exception];
    }
    
    
    return result;
}

- (NSString *)getAdfPushEnv {
    NSString *result;
    
    //    NSLog(@"=== MQTTKEEPALIVEINTERVAL : %d", MQTTKEEPALIVEINTERVAL);
    
    @try {
        
        QueueFile * adfEnv = [ [ADFPush sharedADFPush] adfEnv];
        if ([adfEnv size] > 0) {
            // token Queue read
            NSString *adfEnvJson = [NSString stringWithUTF8String:[[self.adfEnv peek] bytes]];
            result = [NSString stringWithFormat:@"{\"status\": \"ok\",\"code\": 313200,\"message\": \"ADFPUSH 환경정보를 가져왔습니다.\", \"data\": %@}",adfEnvJson];
        }else {
            result = @"{\"status\": \"fail\",\"code\": 313400,\"message\": \"ADFPUSH 환경정보가 없습니다.\"}";
        }
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFError] NSException: %@", exception);
        result = [NSString stringWithFormat:@"{\"status\": \"fail\",\"code\": 313500,\"message\": \"%@\"}",exception];
    }
    
    
    return result;
}


@end


