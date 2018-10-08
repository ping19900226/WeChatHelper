package com.yh.wx.checker;

import com.yh.Config;
import com.yh.common.request.Callback;
import com.yh.wx.entity.*;
import com.yh.wx.request.WeChatRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Arrays;
import java.util.List;

public class MessageChecker {
    private static final Log log = LogFactory.getLog(MessageChecker.class);
    private static MessageChecker messageChecker;
    private ChatChecker chatChecker;

    private MessageChecker() {
        String startKeyWord = Config.get().getStringVal("start.keyword");
        String endKeyWord = Config.get().getStringVal("end.keyword");
        String statisticsKeyword = Config.get().getStringVal("statistics.keyword");
        String[] optionKeyWords = Config.get().getArray("option.keywords");

        startKeyWord = startKeyWord == null ? "开始记录" : startKeyWord;
        endKeyWord = endKeyWord == null ? "结束记录" : endKeyWord;
        statisticsKeyword = statisticsKeyword == null ? "统计" : statisticsKeyword;
        optionKeyWords = optionKeyWords == null || optionKeyWords.length <= 0 ? new String[] {"收到"} : optionKeyWords;

        chatChecker = new RecordChatChecker(startKeyWord, endKeyWord,
            Arrays.asList(optionKeyWords), statisticsKeyword);
    }

    public static MessageChecker get() {
        if(messageChecker == null) {
            messageChecker = new MessageChecker();
        }

        return messageChecker;
    }

    public void start(final WeChatRequestHandler handler, final Callback<Message> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(handler.isHasMessage()) {
                            List<Message> mesgs = handler.webwxsync();

                            for(Message mesg : mesgs) {
                                callback.call(mesg);

                                AuthInfo info = ((AuthInfo) handler.getDefaultAuthenticationInfo()
                                    .getLoginInfo());
                                chatChecker.process(mesg, info, handler);
                            }
                        }

                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        log.error("Thread error: ", e);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {
                            // ignore
                        }
                    } catch (Exception e) {
                        log.error("Failed to send message: " , e);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            // ignore
                        }
                    }
                }
            }
        }).start();
    }
}
