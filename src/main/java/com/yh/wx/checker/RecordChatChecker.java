package com.yh.wx.checker;

import com.yh.wx.entity.*;
import com.yh.wx.request.WeChatRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class RecordChatChecker implements ChatChecker {
    private static final Log log = LogFactory.getLog(RecordChatChecker.class);
    private String startKeyWord;
    private String endKeyWord;
    private boolean checking = false;
    private List<String> optionKeyWords;
    private String statisticsKeyWord;

    public RecordChatChecker(String startKeyWord, String endKeyWord,
        List<String> optionKeyWords, String statisticsKeyWord)
    {
        this.startKeyWord = startKeyWord;
        this.endKeyWord = endKeyWord;
        this.optionKeyWords = optionKeyWords;
        this.statisticsKeyWord = statisticsKeyWord;
    }

    @Override
    public boolean checking() {
        return false;
    }

    @Override
    public void process(Message message, AuthInfo info, WeChatRequestHandler handler) throws
        Exception
    {
        String[] cs = message.getContent().split("<br/>");
        String fromUserName = message.getFromUserName();

        if(cs.length == 1) {
            String[] array = new String[2];
            array[0] = info.getUserName();
            array[1] = cs[0];
            cs = array;
        }

        if(cs[1].startsWith(startKeyWord) || cs[1].startsWith("@" + info.getNickName() + " ")) {
            Contact contact = ContactCache.get().getContact(fromUserName);
            Task task = new Task();
            task.setContent(cs[1]);
            task.setMemberCount(contact.getMemberCount());
            task.setMemberList(contact.getMemberList());
            task.setPublishUserName(cs[0]);
            task.setUsername(fromUserName);
            handler.sendMesg("开始接龙统计", fromUserName);
        }

        Task task = Monitor.get().getTask(fromUserName);

        if(task != null) {
            if(cs[1].equals(endKeyWord)) {
                end(fromUserName, task, handler);
                return;
            }

            if(optionKeyWords.contains(cs[1])) {
                Set<String> readUserList = task.getReadUsers(cs[1]);

                if(readUserList == null) {
                    readUserList = new HashSet<String>();
                    task.setReadUsers(cs[1], readUserList);
                }

                readUserList.add(cs[0]);
            }

            if(cs[1].startsWith(statisticsKeyWord)) {
                statistics(cs[1].replace(statisticsKeyWord, ""), fromUserName, task, handler);
            }
        }

    }

    private void statistics(String mesg, String fromUserName, Task task, WeChatRequestHandler
        handler) throws Exception
    {
        Set<String> users;

        if(mesg != null) {
            users = task.getReadUsers(mesg);
        }
        else {
            users = task.getAllReadUsers();
        }

        String r = users.size() + "/" + task.getMemberCount() +
            "人回复了信息：\n";

        List<String> allUser = new ArrayList<String>();

        for(Map.Entry<String, Contact> entry : task.getMemberList().entrySet()) {
            allUser.add(entry.getKey());
        }

        for(String u : users) {
            Contact c = task.getMemberList().get(u);

            if(c == null) {
                log.info("User empty");
                continue;
            }

            if(c.getUserName().equals(u)) {
                r += c.getDisplayName() == null ? c.getNickName() :
                    c.getDisplayName() + "\n";
            }
        }

        allUser.remove(users);

        r += "以下人员未回复信息\n";

        for(String u : allUser) {
            Contact c = task.getMemberList().get(u);

            if(c.getUserName().equals(u)) {
                r += c.getDisplayName() == null ? c.getNickName() :
                    c.getDisplayName() + "\n";
            }
        }

        handler.sendMesg(r, fromUserName);
    }

    private void end(String fromUserName, Task task, WeChatRequestHandler handler) throws Exception
    {
        Monitor.get().removeTask(fromUserName, null);
        statistics(null, fromUserName, task, handler);
    }
}
