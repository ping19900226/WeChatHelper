package com.yh.util;

import com.alibaba.fastjson.JSON;

public class JSONUtil {

    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T parse(String content, Class<T> clazz) {
       return JSON.parseObject(content, clazz);
    }
}
