package com.yh.wx.entity;

import java.awt.datatransfer.StringSelection;
import java.util.*;

public class Task {
    private String content;
    private String username;
    private String publishUserName;
    private int memberCount;
    //private Map<String, Contact> memberList;
    private Map<String, Set<String>> readUsers = new HashMap<>();

    public Task() {
    }

    public Task(String content, String username, String publishUserName, int memberCount) {
        this.content = content;
        this.username = username;
        this.publishUserName = publishUserName;
        this.memberCount = memberCount;
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

    public Set<String> getReadUsers(String key) {
        return readUsers.get(key);
    }

    public void setReadUsers(String key, Set<String> readUsers) {
        this.readUsers.put(key, readUsers);
    }

    public int countReadUsers() {
        return -1;
    }

    public Set<String> getAllReadUsers() {
        Set<String> users = new HashSet<String>();

        if(readUsers == null) {
            return null;
        }

        for(Map.Entry<String, Set<String>> entry : readUsers.entrySet()) {
            users.addAll(entry.getValue());
        }

        return users;
    }
}
