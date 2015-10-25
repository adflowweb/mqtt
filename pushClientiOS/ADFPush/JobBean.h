//
//  JobBean.h
//  CertTest2
//
//  Created by gwangil on 2014. 6. 26..
//  Copyright (c) 2014년 kamy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JobBean : NSObject

@property int jobId;
@property int msgType;
@property int ack;
@property int qos;
@property (nonatomic, retain) NSString *msgId;
@property (nonatomic, retain) NSString *content;
@property (nonatomic, retain) NSString *contentType;
@property (nonatomic, retain) NSString *topic;
@property (nonatomic, retain) NSString *serviceId;
@property int issueTime;

@end
