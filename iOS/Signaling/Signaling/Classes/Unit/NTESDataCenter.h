//
//  NTESDataCenter.h
//  Signaling
//
//  Created by Netease on 2019/4/24.
//  Copyright © 2019年 Netease. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NTESDataCenter : NSObject

@property (nullable, nonatomic, copy) NSString *account;

@property (nullable, nonatomic, copy) NSString *token;

@property (nullable, nonatomic, strong) NIMSignalingChannelInfo *channelInfo;

+ (instancetype)shareIntance;

@end

NS_ASSUME_NONNULL_END
