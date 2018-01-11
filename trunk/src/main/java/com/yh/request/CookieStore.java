package com.yh.request;

import org.apache.http.impl.client.BasicCookieStore;

import java.net.CookieHandler;

public class CookieStore {

   //现在没有多线程的问题
   public static org.apache.http.client.CookieStore getCookieStore() {
      return store == null ? new BasicCookieStore() : store;
   }

   public static CookieHandler getCookieHander() {
      return new com.yh.request.CookieManager(getCookieStore());
   }

   private static org.apache.http.client.CookieStore store;
}
