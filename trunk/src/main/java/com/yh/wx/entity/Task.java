package com.yh.wx.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {
    private String content;
    private String username;
    private String publishUserName;
    private int memberCount;
    private String owner;
    private Map<String, Contact> memberList;
    private List<String> readUser;

    public Task() {
    }

    public Task(String content, String username, String publishUserName, int memberCount, String owner) {
        this.content = content;
        this.username = username;
        this.publishUserName = publishUserName;
        this.memberCount = memberCount;
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublishUserName() {
        return publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<String, Contact> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Contact> memberList) {
        this.memberList = new HashMap<String, Contact>();

        for(Contact c : memberList) {
            this.memberList.put(c.getUserName(), c);
        }
    }

    public List<String> getReadUser() {
        return readUser;
    }

    public void setReadUser(List<String> readUser) {
        this.readUser = readUser;
    }
}