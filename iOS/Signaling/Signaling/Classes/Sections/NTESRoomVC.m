//
//  NTESRoomVC.m
//  Signaling
//
//  Created by Netease on 2019/4/24.
//  Copyright © 2019年 Netease. All rights reserved.
//

#import "NTESRoomVC.h"

@interface NTESRoomVC ()<UITableViewDelegate, UITableViewDataSource, NIMSignalManagerDelegate>

@property (weak, nonatomic) IBOutlet UILabel *roomIdLab;
@property (weak, nonatomic) IBOutlet UITableView *memberList;

@property (nonatomic, strong) NSMutableArray <NSString *>*msgs;

@end

@implementation NTESRoomVC

- (void)dealloc {
    [[NIMSDK sharedSDK].signalManager removeDelegate:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _msgs = [NSMutableArray array];
    
    NSString *channelId = [NTESDataCenter shareIntance].channelInfo.channelName;
    _roomIdLab.text = [NSString stringWithFormat:@"房间号:%@", (channelId?:@"")];
    
    _memberList.delegate = self;
    _memberList.dataSource = self;
    
    [[NIMSDK sharedSDK].signalManager addDelegate:self];
}


- (IBAction)leaveRoomAction:(id)sender {
    NSString *channelId = [NTESDataCenter shareIntance].channelInfo.channelId;
    NSString *myCount = [NTESDataCenter shareIntance].account;
    NSString *creator = [NTESDataCenter shareIntance].channelInfo.creatorId;
    if ([myCount isEqualToString:creator]) {
        [self doDestoryRoomWithChannelId:channelId];
    } else {
        [self doLeaveRoomWithChannelId:channelId];
    }
}

#pragma mark - Function - LeaveRoom
- (void)doLeaveRoomWithChannelId:(NSString *)channelId {
    __weak typeof(self) weakSelf = self;
    NIMSignalingLeaveChannelRequest *request = [[NIMSignalingLeaveChannelRequest alloc] init];
    request.channelId = channelId;
    [[NIMSDK sharedSDK].signalManager signalingLeaveChannel:request completion:^(NSError * _Nullable error) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"离开房间失败.error:%ld", error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
        } else {
            [NTESDataCenter shareIntance].channelInfo = nil;
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }
    }];
}

- (void)doDestoryRoomWithChannelId:(NSString *)channelId {
    __weak typeof(self) weakSelf = self;
    NIMSignalingCloseChannelRequest *req = [[NIMSignalingCloseChannelRequest alloc] init];
    req.channelId = channelId;
    [[NIMSDK sharedSDK].signalManager signalingCloseChannel:req completion:^(NSError * _Nullable error) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"关闭房间失败。error:%ld", (long)error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
        } else {
            [NTESDataCenter shareIntance].channelInfo = nil;
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }
    }];
}

#pragma mark - Function - DestoryRoom

#pragma mark - <UITableViewDelegate, UITableViewDataSource>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _msgs.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
    }
    cell.textLabel.text = _msgs[indexPath.row];
    return cell;
}

#pragma mark - <NIMChatManagerDelegate>
- (void)nimSignalingOnlineNotifyEventType:(NIMSignalingEventType)eventType
                                 response:(NIMSignalingNotifyInfo *)notifyResponse {
    NSString *msg = [self msgWithEventType:eventType response:notifyResponse];
    if (msg) {
        [_msgs addObject:msg];
        [_memberList reloadData];
    }
    
    if (eventType == NIMSignalingEventTypeClose) {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (NSString *)msgWithEventType:(NIMSignalingEventType)eventType response:(NIMSignalingNotifyInfo *)notifyResponse {
    NSString *ret = nil;
    switch (eventType) {
        case NIMSignalingEventTypeJoin:
            ret = [NSString stringWithFormat:@"%@ 加入房间", notifyResponse.fromAccountId];
            break;
        case NIMSignalingEventTypeLeave:
            ret = [NSString stringWithFormat:@"%@ 离开房间", notifyResponse.fromAccountId];
            break;
        case NIMSignalingEventTypeClose:
        case NIMSignalingEventTypeInvite:
        case NIMSignalingEventTypeCancelInvite:
        case NIMSignalingEventTypeReject:
        case NIMSignalingEventTypeAccept:
        case NIMSignalingEventTypeContrl:
        default:
            break;
    }
    return ret;
}

@end
