package com.yh.request.entity.wx;

public class AuthInfo {
    private String message;
    private String skey;
    private String wxsid;
    private int wxuin;
    private String pass_ticket;
    private int isgrayscale;
    private int ret;
    private String deceiveId;
    private String syncKey;
    private String userName;
    private String nickName;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSkey() {
        return skey;
    }
    public void setSkey(String skey) {
        this.skey = skey;
    }
    public String getWxsid() {
        return wxsid;
    }
    public void setWxsid(String wxsid) {
        this.wxsid = wxsid;
    }
    public int getWxuin() {
        return wxuin;
    }
    public void setWxuin(int wxuin) {
        this.wxuin = wxuin;
    }
    public String getPass_ticket() {
        return pass_ticket;
    }
    public void setPass_ticket(String pass_ticket) {
        this.pass_ticket = pass_ticket;
    }
    public int getIsgrayscale() {
        return isgrayscale;
    }
    public void setIsgrayscale(int isgrayscale) {
        this.isgrayscale = isgrayscale;
    }
    public int getRet() {
        return ret;
    }
    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getDeceiveId() {
        return deceiveId;
    }

    public void setDeceiveId(String deceiveId) {
        this.deceiveId = deceiveId;
    }

    public String getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(String syncKey) {
        this.syncKey = syncKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
