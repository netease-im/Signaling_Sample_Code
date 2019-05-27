//
//  NTESSelectedVC.m
//  Signaling
//
//  Created by Netease on 2019/4/24.
//  Copyright © 2019年 Netease. All rights reserved.
//

#import "NTESSelectedVC.h"

@interface NTESSelectedVC ()<NIMLoginManagerDelegate, UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *roomIdInput;

@end

@implementation NTESSelectedVC

- (void)viewDidLoad {
    [super viewDidLoad];
    _roomIdInput.delegate = self;
}

- (IBAction)loginOutAction:(id)sender {
    __weak typeof(self) weakSelf = self;
    [[NIMSDK sharedSDK].loginManager logout:^(NSError * _Nullable error) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"注销失败。error:%ld", (long)error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
        } else {
            [NTESDataCenter shareIntance].account = nil;
            [NTESDataCenter shareIntance].token = nil;
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }
    }];
}

- (IBAction)createRoomAction:(id)sender {
    NSString *roomName = _roomIdInput.text;
    if (roomName.length == 0) {
        [self.view makeToast:@"房间号为空" duration:2 position:CSToastPositionCenter];
    }
    [self doCreateRoomWithRoomName:roomName];
}

- (IBAction)joinRoomAction:(id)sender {
    NSString *roomName = _roomIdInput.text;
    if (roomName.length == 0) {
        [self.view makeToast:@"房间号为空" duration:2 position:CSToastPositionCenter];
    }
    [self doQueryRoomInfoWithRoomName:roomName];
}

#pragma mark - Function - CreateRoom
- (void)doCreateRoomWithRoomName:(NSString *)roomName {
    __weak typeof(self) weakSelf = self;
    NIMSignalingCreateChannelRequest *request = [[NIMSignalingCreateChannelRequest alloc] init];
    request.channelName = roomName;
    request.channelType = NIMSignalingChannelTypeAudio;
    [[NIMSDK sharedSDK].signalManager signalingCreateChannel:request completion:^(NSError * _Nullable error, NIMSignalingChannelInfo * _Nullable response) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"创建房间失败。error:%ld", (long)error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
        } else {
            [NTESDataCenter shareIntance].channelInfo = response;
            
            //加入房间
            [weakSelf doJoinRoomWithChannelId:response.channelId destoryRoomWhenFail:YES];
        }
    }];
}

#pragma mark - Function - JoinRoom
- (void)doJoinRoomWithChannelId:(NSString *)channelId destoryRoomWhenFail:(BOOL)destoryRoomWhenFail {
    __weak typeof(self) weakSelf = self;
    NIMSignalingJoinChannelRequest *joinReq = [[NIMSignalingJoinChannelRequest alloc] init];
    joinReq.channelId = channelId;
    [[NIMSDK sharedSDK].signalManager signalingJoinChannel:joinReq completion:^(NSError * _Nullable error, NIMSignalingChannelDetailedInfo * _Nullable response) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"加入房间失败。error:%ld", (long)error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
            
            //销毁房间
            if (destoryRoomWhenFail) {
                [weakSelf doDestoryRoomWithChannelId:channelId];
            }
        } else {
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
            UIViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"NTESRoomVC"];
            [weakSelf.navigationController pushViewController:vc animated:YES];
        }
    }];
}

#pragma mark - Function - DestoryRoom
- (void)doDestoryRoomWithChannelId:(NSString *)channelId {
    __weak typeof(self) weakSelf = self;
    NIMSignalingCloseChannelRequest *req = [[NIMSignalingCloseChannelRequest alloc] init];
    req.channelId = channelId;
    [[NIMSDK sharedSDK].signalManager signalingCloseChannel:req completion:^(NSError * _Nullable error) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"关闭房间失败。error:%ld", (long)error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
        }
    }];
}

#pragma mark - Function - QueryRoom
- (void)doQueryRoomInfoWithRoomName:(NSString *)roomName {
    __weak typeof(self) weakSelf = self;
    NIMSignalingQueryChannelRequest *req = [[NIMSignalingQueryChannelRequest alloc] init];
    req.channelName = roomName;
    [[NIMSDK sharedSDK].signalManager signalingQueryChannelInfo:req completion:^(NSError * _Nullable error, NIMSignalingChannelDetailedInfo * _Nullable response) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"查询房间失败。error:%ld", (long)error.code];
            [weakSelf.view makeToast:msg duration:2 position:CSToastPositionCenter];
        } else {
            [NTESDataCenter shareIntance].channelInfo = response;
            
            //加入房间
            [weakSelf doJoinRoomWithChannelId:response.channelId destoryRoomWhenFail:NO];
        }
    }];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    return !(string.length != 0 && textField.text.length > 7);
}

@end
