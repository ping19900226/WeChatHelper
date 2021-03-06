package com.yh.wx.checker;

import com.yh.wx.entity.*;
import com.yh.wx.request.WeChatRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 用于统计指定回复记录
 */
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
        //对于别人在群里面发送的消息，ToUserName 是自己，FromUserName 是群 Content 包含 发送人:<br/>内容

        if(cs.length == 1) {
            String[] array = new String[2];
            array[0] = info.getUserName();
            array[1] = cs[0];
            cs = array;
            //对于自己在群里面发送的消息，ToUserName 是群  FromUserName是自己
            fromUserName = message.getToUserName();
        }
        else {
            cs[0] = cs[0].replace(":", "");
        }

        String name = info.getDisplayName() != null && !info.getDisplayName().isEmpty() ? info.getDisplayName() :
            info.getNickName();

        if(cs[1].contains("@" + name + " ")) {
            atMe(fromUserName, cs[0], cs[1], name, handler);
        }

        if(cs[1].startsWith(startKeyWord)) {
            String content = cs[1].replace(startKeyWord, "");
            addTask(fromUserName, cs[0], content, handler);
        }

        checkAndStatistics(fromUserName, cs[0], cs[1], handler);
    }

    private void atMe(String fromUserName, String sendUserName, String message, String name,
        WeChatRequestHandler handler) throws Exception
    {
        String text = message;
        String[] ks = text.split("@" + name);

        String content = ks[0];

        // @小助手 开始记录 收到 结束记录 统计

        String kwStr = ks[1];
        String[] kws = kwStr.split(" ");

        if(kws.length >= 1) {
            startKeyWord = kws[0].isEmpty() ? startKeyWord : kws[0];
        }

        if(kws.length >= 2) {
            startKeyWord = kws[1].isEmpty() ? startKeyWord : kws[1];
        }

        if(kws.length >= 3) {
            startKeyWord = kws[2].isEmpty() ? startKeyWord : kws[2];
        }

        if(kws.length >= 4) {
            startKeyWord = kws[3].isEmpty() ? startKeyWord : kws[3];
        }

        addTask(fromUserName, sendUserName, content, handler);
    }

    private void addTask(String fromUserName, String sendUserName, String message, WeChatRequestHandler handler)
        throws Exception
    {
        Contact contact = ContactCache.get().getContact(fromUserName);
        Task task = new Task();
        task.setContent(message);
        task.setMemberCount(contact.getMemberCount());
        task.setPublishUserName(sendUserName);
        task.setUsername(fromUserName);
        Monitor.get().monitor(fromUserName);
        Monitor.get().addTask(fromUserName, task);
        log.info("Start to record, [nick_name=" + contact.getNickName() + ", display_name=" +
            contact.getDisplayName() + ", member_count=" + task.getMemberCount() +
            "， start_keyword=" + startKeyWord + ", end_keyword=" + endKeyWord + ", " +
            "statistics_keyword=" + statisticsKeyWord + ", replay_keyword=" +
            Arrays.toString(optionKeyWords.toArray()));

        String answer = "";

        for(String opw : optionKeyWords) {
            if(answer.length() > 0) {
                answer += " 或者回复 ";
            }

            answer += opw;
        }

        handler.sendMesg("请回复：" + answer, fromUserName);

        List<Contact> contactDetails = handler.batchGetContact(contact.getEncryChatRoomId(),
            contact.getMemberList());
        task.setMemberList(contactDetails);
    }

    private void checkAndStatistics(String fromUserName, String sendUserName, String message,
        WeChatRequestHandler handler) throws Exception
    {
        for(String uu : Monitor.get().getMonitor()) {
            if(fromUserName.equals(uu)) {
                Task task = Monitor.get().getTask(fromUserName);

                if(task != null) {
                    if(message.equals(endKeyWord)) {
                        end(fromUserName, task, handler);
                        return;
                    }

                    if(optionKeyWords.contains(message)) {
                        Set<String> readUserList = task.getReadUsers(message);

                        if(readUserList == null) {
                            readUserList = new HashSet<String>();
                            task.setReadUsers(message, readUserList);
                        }

                        readUserList.add(sendUserName);
                    }

                    if(message.startsWith(statisticsKeyWord)) {
                        statistics(message.replace(statisticsKeyWord, ""), fromUserName, task, handler);
                    }
                }
            }
        }
    }

    private void end(String fromUserName, Task task, WeChatRequestHandler handler) throws Exception
    {
        Monitor.get().removeTask(fromUserName, null);
        statistics(null, fromUserName, task, handler);
    }

    private void statistics(String mesg, String fromUserName, Task task, WeChatRequestHandler
        handler) throws Exception
    {
        handler.sendMesg("正在进行回复信息统计，请稍等...", fromUserName);
        Set<String> users;

        if(!mesg.isEmpty()) {
            users = task.getReadUsers(mesg);
        }
        else {
            users = task.getAllReadUsers();
        }

        users = users == null ? new HashSet<>() : users;

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

            r += (c.getDisplayName() == null || c.getDisplayName().isEmpty() ? c.getNickName() :
                c.getDisplayName()) + "\n";
        }

        allUser.removeAll(users);

        r += "以下人员未回复信息\n";

        for(String u : allUser) {
            Contact c = task.getMemberList().get(u);

            if(c == null) {
                log.info("User empty");
                continue;
            }

            r += (c.getDisplayName() == null || c.getDisplayName().isEmpty() ? c.getNickName() :
                c.getDisplayName()) + "\n";
        }

        log.info("Statistics: " + r);
        handler.sendMesg(r, fromUserName);
    }
}
