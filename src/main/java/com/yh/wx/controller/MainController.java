package com.yh.wx.controller;

import com.alibaba.fastjson.JSON;
import com.yh.common.request.Callback;
import com.yh.common.request.RequestHandler;
import com.yh.wx.checker.MessageChecker;
import com.yh.wx.entity.*;
import com.yh.wx.request.WeChatRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);
    private WeChatRequestHandler handler;
    private MessageChecker messageChecker;

    public MainController() {
        handler = (WeChatRequestHandler) RequestHandler.getRequestHandler(3);
    }

    public List<Contact> getContactList() {
        List<Contact> contacts =  handler.getContactList();

        try {
            List<Contact> contacts0 =  handler.getContactInitList();

            for(Contact contact : contacts0) {
                if(!contacts.contains(contact)) {
                    contacts.add(contact);
                }
            }
        }
        catch(Exception e) {
            log.info("Get init contact list error.", e);
        }

        sort(contacts);

        ContactCache.get().setContactList(contacts);
        return contacts;
    }

    public boolean sendMessage(String content, String toUserName) {
        try {
            return handler.sendMesg(content, toUserName);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void startProcess(Callback<Message> callback) {
        MessageChecker.get().start(handler, callback);
    }

    private void sort(List<Contact> contacts) {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getpYQuanPin().compareToIgnoreCase(o2.getpYQuanPin());
            }
        });
    }
}
