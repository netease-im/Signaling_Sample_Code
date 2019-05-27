//
//  NTESDataCenter.m
//  Signaling
//
//  Created by Netease on 2019/4/24.
//  Copyright © 2019年 Netease. All rights reserved.
//

#import "NTESDataCenter.h"

@implementation NTESDataCenter

+ (instancetype)shareIntance {
    static id instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[NTESDataCenter alloc] init];
    });
    return instance;
}

@end
