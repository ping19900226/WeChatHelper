package com.yh.request;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;

import java.net.CookieHandler;

public class RequestHelper {

   private static CookieStore store;
   private static CloseableHttpClient client;
   private static CookieManager manager;

   static {
      store = new BasicCookieStore();
      HttpClientContext localContext = HttpClientContext.create();
      localContext.setCookieStore(store);
      client = HttpClients.custom().setDefaultCookieStore(store).build();
      manager = new CookieManager(store);
   }

   //现在没有多线程的问题
   public static org.apache.http.client.CookieStore getCookieStore() {
      return store;
   }

   public static CookieHandler getCookieHander() {
      return manager;
   }

   public static CloseableHttpClient getHttpClient() {
      return client;
   }
}
