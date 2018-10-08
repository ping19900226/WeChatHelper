package com.yh.wx.entity;

import java.util.List;

public class Contact {

    public static final int CONTACT_FLAG_PERSONAGE = 2115;
    public static final int CONTACT_FLAG_WECHAT_SUBCRIBE = 3;
    public static final int CONTACT_FLAG_GROUP = 0;
    public static final int CONTACT_FLAG_WECHAT_SERVICE = 2051;
    public static final int CONTACT_FLAG_WECHAT= 1;
    public static final int CONTACT_FLAG_FILE_HELPER = 2;
    public static final int CONTACT_FLAG_WECHAT_YUNDONG = 3;
    public static final int CONTACT_FLAG_WECHAT_TUANDUI = 56;

    private int uin;
    private String userName;
    private String nickName;
    private String headImgUrl;
    private int contactFlag;
    private int memberCount;
    private List<Contact> memberList;
    private String remarkName;
    private int hideInputBarFlag;
    private int sex;
    private String signature;
    private int verifyFlag;
    private int ownerUin;
    private String pYInitial;
    private String pYQuanPin;
    private String remarkPYInitial;
    private String remarkPYQuanPin;
    private int starFriend;
    private int appAccountFlag;
    private int statues;
    private int attrStatus;
    private String province;
    private String city;
    private String alias;
    private int snsFlag;
    private int uniFriend;
    private String displayName;
    private int chatRoomId;
    private String keyWord;
    private String encryChatRoomId;
    private int isOwner;
    private boolean initMenber;

    public int getUin() {
        return uin;
    }

    public void setUin(int uin) {
        this.uin = uin;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getContactFlag() {
        return contactFlag;
    }

    public void setContactFlag(int contactFlag) {
        this.contactFlag = contactFlag;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public List<Contact> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Contact> memberList) {
        this.memberList = memberList;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public int getHideInputBarFlag() {
        return hideInputBarFlag;
    }

    public void setHideInputBarFlag(int hideInputBarFlag) {
        this.hideInputBarFlag = hideInputBarFlag;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(int verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public int getOwnerUin() {
        return ownerUin;
    }

    public void setOwnerUin(int ownerUin) {
        this.ownerUin = ownerUin;
    }

    public String getpYInitial() {
        return pYInitial;
    }

    public void setpYInitial(String pYInitial) {
        this.pYInitial = pYInitial;
    }

    public String getpYQuanPin() {
        return pYQuanPin;
    }
    public void setpYQuanPin(String pYQuanPin) {
        this.pYQuanPin = pYQuanPin;
    }

    public String getRemarkPYInitial() {
        return remarkPYInitial;
    }

    public void setRemarkPYInitial(String remarkPYInitial) {
        this.remarkPYInitial = remarkPYInitial;
    }

    public String getRemarkPYQuanPin() {
        return remarkPYQuanPin;
    }

    public void setRemarkPYQuanPin(String remarkPYQuanPin) {
        this.remarkPYQuanPin = remarkPYQuanPin;
    }
    public int getStarFriend() {
        return starFriend;
    }

    public void setStarFriend(int starFriend) {
        this.starFriend = starFriend;
    }

    public int getAppAccountFlag() {
        return appAccountFlag;
    }

    public void setAppAccountFlag(int appAccountFlag) {
        this.appAccountFlag = appAccountFlag;
    }

    public int getStatues() {
        return statues;
    }

    public void setStatues(int statues) {
        this.statues = statues;
    }

    public int getAttrStatus() {
        return attrStatus;
    }

    public void setAttrStatus(int attrStatus) {
        this.attrStatus = attrStatus;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getSnsFlag() {
        return snsFlag;
    }

    public void setSnsFlag(int snsFlag) {
        this.snsFlag = snsFlag;
    }

    public int getUniFriend() {
        return uniFriend;
    }

    public void setUniFriend(int uniFriend) {
        this.uniFriend = uniFriend;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getEncryChatRoomId() {
        return encryChatRoomId;
    }

    public void setEncryChatRoomId(String encryChatRoomId) {
        this.encryChatRoomId = encryChatRoomId;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isIwner) {
        this.isOwner = isOwner;
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact contact = (Contact) o;

        if(uin != contact.uin) {
            return false;
        }
        if(chatRoomId != contact.chatRoomId) {
            return false;
        }
        if(!userName.equals(contact.userName)) {
            return false;
        }
        return nickName.equals(contact.nickName);
    }

    @Override public int hashCode() {
        int result = uin;
        result = 31 * result + userName.hashCode();
        result = 31 * result + nickName.hashCode();
        result = 31 * result + chatRoomId;
        return result;
    }
}
