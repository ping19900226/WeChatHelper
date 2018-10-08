package com.yh.wx.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class ContactCache {
    private static ContactCache contactCache;
    private Log log = LogFactory.getLog(ContactCache.class);

    private ContactCache() {

    }

    public static ContactCache get() {
        if(contactCache == null) {
            contactCache = new ContactCache();
        }

        return contactCache;
    }

    private Map<String, Contact> contactList = new HashMap<String, Contact>();

    public List<Contact> getContactList() {
        List<Contact> contacts = new ArrayList<Contact>();

        for(Map.Entry<String, Contact> entry : contactList.entrySet()) {
            contacts.add(entry.getValue());
        }

        return contacts;
    }

    public void setContactList(List<Contact> contactList) {
        for(Contact c : contactList) {
            this.contactList.put(c.getUserName(), c);
        }
    }

    public Contact getContact(String username) {
        return contactList.get(username);
    }

    public void addContact(Contact con) {
        contactList.put(con.getUserName(), con);
    }
}
