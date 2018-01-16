package com.yh.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

public class JSONUtil {

   public static String toJSONString(Object obj) {
      return JSON.toJSONString(obj);
   }

   public static <T> T parse(String content, Class<T> clazz) {
      return JSON.parseObject(content, clazz);
   }

   public static String serialize(String param) {
      JSONObject jsonObject = JSON.parseObject(param);
      StringBuilder rs = new StringBuilder();
      Iterator iterator = jsonObject.keySet().iterator();

      while(iterator.hasNext()) {
         String key = (String) iterator.next();
         String value = jsonObject.getString(key);
         rs.append(key).append("=").append(value).append("&");
      }
      return rs.toString();
   }
}
