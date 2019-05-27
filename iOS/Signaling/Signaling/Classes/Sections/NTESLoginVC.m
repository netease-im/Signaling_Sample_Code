//
//  NTESLoginVC.m
//  Signaling
//
//  Created by Netease on 2019/4/23.
//  Copyright © 2019年 Netease. All rights reserved.
//

#import "NTESLoginVC.h"
#import "NSString+NTES.h"

@interface NTESLoginVC ()
@property (weak, nonatomic) IBOutlet UITextField *accountInput;
@property (weak, nonatomic) IBOutlet UITextField *passwordInput;
@end

@implementation NTESLoginVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (IBAction)loginAction:(id)sender {
    
    NSString *username = [_accountInput.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSString *password = _passwordInput.text;
    NSString *loginAccount = username;
    NSString *loginToken   = [password tokenByPassword];
    if (username.length == 0) {
        [self.view makeToast:@"账号为空" duration:2 position:CSToastPositionCenter];
        return;
    }
    if (password.length == 0) {
        [self.view makeToast:@"密码为空" duration:2 position:CSToastPositionCenter];
        return;
    }
    
    [[NIMSDK sharedSDK].loginManager login:loginAccount token:loginToken completion:^(NSError * _Nullable error) {
        if (error) {
            NSString *msg = [NSString stringWithFormat:@"登陆失败.error:%ld", (long)error.code];
            [self.view makeToast:msg duration:2 position:CSToastPositionCenter];
        } else {
            
            [NTESDataCenter shareIntance].account = loginAccount;
            [NTESDataCenter shareIntance].token = loginToken;
            
            UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
            UIViewController *vc = [storyBoard instantiateViewControllerWithIdentifier:@"NTESSelectedVC"];
            [self.navigationController pushViewController:vc animated:YES];
        }
    }];
    
}

@end
