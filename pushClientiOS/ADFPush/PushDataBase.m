//
//  PushDataBase.m
//  ADFlowOfficeADFlowOfficeIphone
//
//  Created by gwangil on 2014. 6. 15..
//
//

#import "PushDataBase.h"

@implementation PushDataBase

- (void)initWithDataBase
{
    sqlite3_stmt *statement = nil;
    sqlite3 *pDataBase;
    @try {
 
        [self dataBaseConnection:&pDataBase];     // 데이터베이스 연결합니다.
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return;
        }
        
           const char* sql = "SELECT count(*) from job";
            
        /* job을 쿼리해보고 오류가 있으면 job table을 생성한다. */
        if (sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK) {
            
            NSLog(@"[ADFPush] Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            /* 테이블 생성 */
            if (sqlite3_prepare_v2(pDataBase, "CREATE TABLE job ( jobid INTEGER PRIMARY KEY AUTOINCREMENT, msgtype INTEGER, ack INTEGER, qos INTEGER,msgid TEXT, content TEXT, contenttype TEXT, topic TEXT, serviceid TEXT, issuetime INTEGER);", -1, &statement, NULL) != SQLITE_OK) {
                NSLog(@"[ADFPush] TABLE CREATE ERROR: %s", sqlite3_errmsg(pDataBase));
            }else {
                sqlite3_step(statement);
                NSLog(@"[ADFPush] job table CREATE OK");
            }
        }
        
//        int count;
//        //쿼리를 실행한다.
//        while(sqlite3_step(statement) == SQLITE_ROW) {
//            
//            count = sqlite3_column_int(statement,0);
//            
//            NSLog(@"== Message UnRead Count : %d", count);
//        }

        
        
    }
    @catch (NSException *exception) {
        NSLog(@"exceptionName %@, reason %@", [exception name], [exception reason]);
    }
    @finally {
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
    
}


- (void)dataBaseConnection:(sqlite3 **)tempDataBase
{
    // Document 폴더 위치를 구합니다.
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDirectory = [paths objectAtIndex:0];
    
    
    // 데이터베이스 파일경로를 구합니다.
	NSString *myPath = [documentDirectory stringByAppendingPathComponent:@"ADFPush.db"];
    
	
    //데이터 베이스 연결
	if (sqlite3_open([myPath UTF8String], tempDataBase) != SQLITE_OK) {
		*tempDataBase = nil;
		return;
	}
	
}


/// Job Insert
- (int) insertJob:(JobBean *)job
{
    sqlite3_stmt *statement = nil;
    sqlite3 *pDataBase;
    int jobId = -1;
    
    @try {
        NSLog(@"======  Job instert - Start");
        
        
        [self dataBaseConnection:&pDataBase];     // 데이터베이스 연결합니다.
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return -1;
        }
        
        const char *sql = "INSERT INTO job(msgtype, ack, qos, msgid, content, contenttype, topic, serviceid, issuetime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            //            sqlite3_close(pDataBase);   //데이터베이스를 닫는다
            //            pDataBase = nil;
            return -1;
        }
        
        // 조건을 바인딩합니다.
        sqlite3_bind_int(statement, 1, job.msgType);
        sqlite3_bind_int(statement, 2, job.ack);
        sqlite3_bind_int(statement, 3, job.qos);
        sqlite3_bind_text(statement, 4, [job.msgId UTF8String], -1, SQLITE_TRANSIENT);
        sqlite3_bind_text(statement, 5, [job.content UTF8String], -1, SQLITE_TRANSIENT);
        sqlite3_bind_text(statement, 6, [job.contentType UTF8String], -1, SQLITE_TRANSIENT);
        sqlite3_bind_text(statement, 7, [job.topic UTF8String], -1, SQLITE_TRANSIENT);
        sqlite3_bind_text(statement, 8, [job.serviceId UTF8String], -1, SQLITE_TRANSIENT);
        sqlite3_bind_int(statement, 9, job.issueTime);
        
        
        
        //쿼리를 실행한다.
        int resultCode = sqlite3_step(statement);
        NSLog(@"resultCode : '%d'", resultCode);
        
        if( resultCode != SQLITE_DONE) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return -1;
        }
        
        // 검색 SQL
        const char *sql2 = "SELECT jobid FROM job where msgid = ?";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql2, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return -1;
            
        }
        
        // 조건을 바인딩합니다.
        sqlite3_bind_text(statement, 1, [job.msgId UTF8String], -1, SQLITE_TRANSIENT);
        
        //쿼리를 실행한다.
        while(sqlite3_step(statement) == SQLITE_ROW) {
            
            jobId = sqlite3_column_int(statement,0);
            
            NSLog(@"jobId : %d ", jobId);
        }
        
    }
    @catch (NSException *exception) {
        NSLog(@"exceptionName %@, reason %@", [exception name], [exception reason]);
    }
    @finally {
        
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
    
    return jobId;
}


// Job Select
- (NSArray *) getJobList
{
    NSLog(@"======  getJobList - Start ");
    
	sqlite3_stmt *statement = nil;
	sqlite3 *pDataBase;
    NSMutableArray *jobList = [NSMutableArray array];
    
    
    @try {
        [self dataBaseConnection:&pDataBase];    // 데이터베이스 연결
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return NULL;
        }
        
        // 검색 SQL
        const char *sql = "SELECT jobid,msgtype, ack, qos, msgid, content, contenttype, topic, serviceid, issuetime FROM job";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            sqlite3_close(pDataBase);   //데이터베이스를 닫는다
            pDataBase = nil;
            return NULL;
            
        }
        
        
		
		//쿼리를 실행한다.
        while(sqlite3_step(statement) == SQLITE_ROW) {
            
            JobBean *job = [[JobBean alloc]init];
            
            [job setJobId:sqlite3_column_int(statement,0)];
            [job setMsgType:sqlite3_column_int(statement,1)];
            [job setAck:sqlite3_column_int(statement,2)];
            [job setQos:sqlite3_column_int(statement,3)];
			[job setMsgId:[[NSString alloc] initWithString:[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 4)]]];
            [job setContent:[[NSString alloc] initWithString:[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 5)]]];
            [job setContentType:[[NSString alloc] initWithString:[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 6)]]];
            [job setTopic:[[NSString alloc] initWithString:[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 7)]]];
            [job setServiceId:[[NSString alloc] initWithString:[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 8)]]];
            [job setIssueTime:sqlite3_column_int(statement, 9)];

            [jobList addObject:job];
            NSLog(@"Message : %d, %d, %d, %d, %@, %@, %@, %@, %@, %d", job.jobId,job.msgType,job.ack,job.qos,job.msgId,job.content,job.contentType,job.topic,job.serviceId, job.issueTime);
        }
        
        return (NSArray *) jobList;
        
    }
    @catch (NSException *exception) {
        NSLog(@"[ADFPush] getJobList - exceptionName %@, reason %@", [exception name], [exception reason]);
    }
    @finally {
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
	
    
}

// Job Delete
- (void) deleteJob:(NSString *)msgId
{
    sqlite3_stmt *statement = nil;
    sqlite3 *pDataBase;
    
    @try {
        NSLog(@"======  deleteJob ID:%@ - Start ",msgId);
        
        
        [self dataBaseConnection:&pDataBase];     // 데이터베이스 연결합니다.
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return;
        }
        
        const char *sql = "DELETE FROM job WHERE msgid=?";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            //            sqlite3_close(pDataBase);   //데이터베이스를 닫는다
            //            pDataBase = nil;
            return;
            
            
        }
        
        // 조건을 바인딩합니다.
        sqlite3_bind_text(statement, 1, [msgId UTF8String], -1, SQLITE_TRANSIENT);
        
        
        //쿼리를 실행한다.
        int resultCode = sqlite3_step(statement);
        NSLog(@"resultCode : '%d'", resultCode);
        
        if( resultCode != SQLITE_DONE) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            
        }
        
    }
    @catch (NSException *exception) {
        NSLog(@"exceptionName %@, reason %@", [exception name], [exception reason]);
    }
    @finally {
        
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
    
}

- (void) deleteJobId:(int)jobId
{
    sqlite3_stmt *statement = nil;
    sqlite3 *pDataBase;
    
    @try {
        NSLog(@"======  deleteJob ID:%d - Start ",jobId);
        
        
        [self dataBaseConnection:&pDataBase];     // 데이터베이스 연결합니다.
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return;
        }
        
        const char *sql = "DELETE FROM job WHERE jobid=?";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            //            sqlite3_close(pDataBase);   //데이터베이스를 닫는다
            //            pDataBase = nil;
            return;
            
            
        }
        
        // 조건을 바인딩합니다.
        sqlite3_bind_int(statement, 1, jobId);
        
        
        //쿼리를 실행한다.
        int resultCode = sqlite3_step(statement);
        NSLog(@"resultCode : '%d'", resultCode);
        
        if( resultCode != SQLITE_DONE) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            
        }
        
    }
    @catch (NSException *exception) {
        NSLog(@"exceptionName %@, reason %@", [exception name], [exception reason]);
    }
    @finally {
        
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
    
}


- (NSString * ) deleteJobAll {
    sqlite3_stmt *statement = nil;
    sqlite3 *pDataBase;
    NSString *result;
    
    @try {
        NSLog(@"======  deleteJobAll - Start ");
        
        
        [self dataBaseConnection:&pDataBase];     // 데이터베이스 연결합니다.
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            result = [NSString stringWithUTF8String:(char*)sqlite3_errmsg(pDataBase)];
            return result;
        }
        
        const char *sql = "DELETE FROM job";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            result = [NSString stringWithUTF8String:(char*)sqlite3_errmsg(pDataBase)];
            return result;
            
            
        }
        
        //쿼리를 실행한다.
        int resultCode = sqlite3_step(statement);
        NSLog(@"resultCode : '%d'", resultCode);
        
        if( resultCode != SQLITE_DONE) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            result = [NSString stringWithUTF8String:(char*)sqlite3_errmsg(pDataBase)];
            return result;
        }
        
        result = @"OK";
    }
    @catch (NSException *exception) {
        NSLog(@"exceptionName %@, reason %@", [exception name], [exception reason]);
        result = [NSString stringWithFormat:@"%@",exception];
    }
    @finally {
        
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
    return result;
}



- (int) getAck:(int) jobId // Ack 여부
{
    NSLog(@"======  getAck - Start ");
    
    sqlite3_stmt *statement = nil;
    sqlite3 *pDataBase;
    int ack = 2;
    
    @try {
        [self dataBaseConnection:&pDataBase];    // 데이터베이스 연결
        if (pDataBase == nil) {
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return 2;
        }
        
        // 검색 SQL
        const char *sql = "SELECT ack FROM job where jobid = ?";
        
        // SQL Text를 prepared statement로 변환합니다.
        if(sqlite3_prepare_v2(pDataBase, sql, -1, &statement, NULL) != SQLITE_OK)
        {
            
            NSLog(@"Erro Message : '%s'", sqlite3_errmsg(pDataBase));
            return 2;
            
        }
        
        // 조건을 바인딩합니다.
        sqlite3_bind_int(statement, 1, jobId);
        
        //쿼리를 실행한다.
        while(sqlite3_step(statement) == SQLITE_ROW) {
            
            ack = sqlite3_column_int(statement,0);
            
            NSLog(@"ack : %d ", ack);
        }
    }
    @catch (NSException *exception) {
        NSLog(@"exceptionName %@, reason %@", [exception name], [exception reason]);
    }
    @finally {
        sqlite3_reset(statement);   //객체 초기화
        sqlite3_finalize(statement);  //객체를 닫는다
        sqlite3_close(pDataBase);   //데이터베이스를 닫는다
        pDataBase = nil;
        
    }
    
    return ack;
}


@end
