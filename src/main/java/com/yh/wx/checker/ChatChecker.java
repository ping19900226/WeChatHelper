package com.yh.wx.checker;

import com.yh.wx.entity.AuthInfo;
import com.yh.wx.entity.Message;
import com.yh.wx.request.WeChatRequestHandler;

public interface ChatChecker {
    boolean checking();
    void process(Message message, AuthInfo info, WeChatRequestHandler handler) throws Exception;
}
