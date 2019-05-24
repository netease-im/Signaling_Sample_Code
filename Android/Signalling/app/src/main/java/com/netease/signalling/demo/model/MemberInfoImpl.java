package com.netease.signalling.demo.model;

/**
 * Created by hzsunyj on 2019-05-06.
 */
public class MemberInfoImpl {

    public static final int JOIN_TYPE = 0;

    public static final int LEAVE_TYPE = 1;

    public String account;

    public int type;

    public MemberInfoImpl(int type, String account) {
        this.type = type;
        this.account = account;
    }
}
