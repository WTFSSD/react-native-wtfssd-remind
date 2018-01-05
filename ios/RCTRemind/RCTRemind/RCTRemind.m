//
//  RCTRemind.m
//  RCTRemind
//
//  Created by WTFSSD on 2018/1/5.
//  Copyright © 2018年 marhub. All rights reserved.
//

#import "RCTRemind.h"
#import <React/RCTUtils.h>
@interface RCTRemind()

@property(nonatomic)RCTPromiseResolveBlock resolve;
@property(nonatomic)RCTPromiseRejectBlock reject;
@property(nonatomic,strong)NSDictionary * params;
@end
@implementation RCTRemind

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(addEvent:(NSDictionary *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    
    self.resolve = resolve;
    self.reject = reject;
    
    self.params = params;
    
    
    
    
    
    
    
    
    EKEventStore *store = [[EKEventStore alloc] init];
    EKEvent *event = [EKEvent eventWithEventStore:store];
    NSDateFormatter * formatter = [[NSDateFormatter alloc] init];
    
  
    
    if(params) {
        NSLog(@"创建参数为:%@\n",params);
        if(params[@"title"]) {
            event.title =params[@"title"];
//            NSLog(@"1=>%@",params[@"title"]);
        }
        if(params[@"location"]) {
            event.location =params[@"location"];
//            NSLog(@"2=>%@",params[@"location"]);
        }
        if(params[@"allDay"]) {
            event.allDay =[params[@"allDay"] boolValue];
//            NSLog(@"3=%d",[params[@"allDay"] boolValue]);
        }
        if(params[@"startDate"]) {
            event.startDate = [self getNowDate:params[@"startDate"]];
//            NSLog(@"4=>%@",[formatter dateFromString:params[@"startDate"]]);
        }
        if(params[@"endDate"]) {
            event.endDate = [self getNowDate:params[@"endDate"]];
//            NSLog(@"5=>%@",[formatter dateFromString: params[@"endDate"]]);
        }
        if(params[@"URL"]) {
            event.URL =[NSURL URLWithString:params[@"URL"]];
//            NSLog(@"6=>%@",[NSURL URLWithString:params[@"URL"]]);
        }
        if(params[@"notes"]) {
            event.notes =params[@"notes"];
//            NSLog(@"7=>%@",params[@"notes"]);
        }
    }
    
//    event.title=title;
//    event.location=location;
//
//    event.allDay = allDay;
//    event.startDate = startDate;
//    event.endDate = endDate;
//
//    event.URL = URL;
//    event.notes = notes;
    
    EKEventEditViewController *vc = [[EKEventEditViewController alloc] init];
    vc.eventStore = store;
    vc.event = event;
    vc.editViewDelegate = self;
    if([store respondsToSelector:@selector(requestAccessToEntityType:completion:)]) {
        [store requestAccessToEntityType:EKEntityTypeEvent completion:^(BOOL granted, NSError *error) {
            if(granted){
                dispatch_async(dispatch_get_main_queue(), ^{
                    UIViewController *root = RCTPresentedViewController();
                    [root presentViewController:vc animated:YES completion:nil];
                });
            
            }else{
                self.reject(@"403", @"permissions denied", nil);
            }
            /* This code will run when uses has made his/her choice */
        }];
    }
}

- (void)eventEditViewController:(EKEventEditViewController *)controller didCompleteWithAction:(EKEventEditViewAction)action{
    
    
    
    /**/
    dispatch_block_t dismissCompletionBlock = ^{
        switch (action) {
            case EKEventEditViewActionCanceled:
                self.reject(@"401", @"user canceled", nil);
                break;
            case EKEventEditViewActionSaved:
                self.resolve(@(YES));
                break;
            case EKEventEditViewActionDeleted:
                self.reject(@"402", @"event hase deleted", nil);
            default:
                break;
        }
    };
    dispatch_async(dispatch_get_main_queue(), ^{
        [controller dismissViewControllerAnimated:YES completion:dismissCompletionBlock];
    });
   
}


- (NSDate *)getNowDate:(id )dateStr {
    
    if(!dateStr) return nil;
    
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"YYYY-MM-DD HH:mm:ss"];
//
    NSDate *anyDate = [dateFormatter dateFromString:dateStr];
//
//
//    //设置源日期时区
//    NSTimeZone* sourceTimeZone = [NSTimeZone timeZoneWithAbbreviation:@"UTC"];//或GMT
//    //设置转换后的目标日期时区
//    NSTimeZone* destinationTimeZone = [NSTimeZone localTimeZone];
//    //得到源日期与世界标准时间的偏移量
//    NSInteger sourceGMTOffset = [sourceTimeZone secondsFromGMTForDate:anyDate];
//    //目标日期与本地时区的偏移量
//    NSInteger destinationGMTOffset = [destinationTimeZone secondsFromGMTForDate:anyDate];
//    //得到时间偏移量的差值
//    NSTimeInterval interval = destinationGMTOffset - sourceGMTOffset;
//    //转为现在时间
//    NSDate* destinationDateNow = [[NSDate alloc] initWithTimeInterval:interval sinceDate:anyDate];
    
    
//    NSLog(@"转化时间:%@\n前:%@\n",dateStr,anyDate);
//    NSLog(@"转化时间:%@",dateStr);
    
    return anyDate;
}
@end
