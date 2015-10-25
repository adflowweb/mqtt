//
//  PushDataBase.h
//  ADFlowOfficeADFlowOfficeIphone
//
//  Created by gwangil on 2014. 6. 15..
//
//

//#import <Foundation/Foundation.h>
#import "sqlite3.h"
#import "JobBean.h"




@interface PushDataBase : NSObject

- (void)initWithDataBase;
- (void) dataBaseConnection:(sqlite3 **)tempDataBase;
- (NSArray *) getJobList;
- (int) insertJob:(JobBean *)job;
- (void) deleteJob:(NSString *) msgId;
- (void) deleteJobId:(int) jobId;
- (NSString *) deleteJobAll;
- (int) getAck:(int) jobId;
@end
