package com.yh.request;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class RequestHandler {

   {
      HttpClientContext localContext = HttpClientContext.create();
      localContext.setCookieStore(CookieStore.getCookieStore());
      client = HttpClients.custom().setDefaultCookieStore(CookieStore.getCookieStore()).build();
   }

   public void setDefaultAuthenticationInfo(AuthenticationInfo info) {
      this.info = info;
   }

   public AuthenticationInfo getDefaultAuthenticationInfo() {
      return info;
   }

   public abstract void login(String username, String password);

   protected void get(String url, ResponseHandler handler) throws Exception {
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response = client.execute(httpGet);

      try{
         StatusLine status = response.getStatusLine();
         HttpEntity entity = response.getEntity();
         handler.handle(new Response(status, entity, response.getAllHeaders()));
         EntityUtils.consume(entity);
      }
      finally {
         response.close();
      }
   }

   protected void post(String url, Params param, ResponseHandler handler) throws Exception {
      HttpPost httpPost = new HttpPost(url);

      httpPost.setEntity(new UrlEncodedFormEntity(param.toNameValuePair()));
      CloseableHttpResponse response = client.execute(httpPost);

      try {
         System.out.println(response.getStatusLine());
         StatusLine status = response.getStatusLine();
         HttpEntity entity = response.getEntity();
         // do something useful with the response body
         // and ensure it is fully consumed
         handler.handle(new Response(status, entity, response.getAllHeaders()));
         EntityUtils.consume(entity);
      }
      finally {
         response.close();
      }
   }

   interface ResponseHandler {

      public void handle(Response response) throws Exception;
   }

   class Response {
      private StatusLine status;
      private HttpEntity entity;
      private Map<String, String> headers = new HashMap<String, String>();

      private Response() {

      }

      private Response(StatusLine status, HttpEntity entity, Header[] headers) {
         this.status = status;
         this.entity = entity;

         for(Header header : headers) {
            this.headers.put(header.getName(), header.getValue());
         }
      }

      public int getStatusCode() {
         return status.getStatusCode();
      }

      public Header getContentType() {
         return entity.getContentType();
      }

      public InputStream getContent() {
         try {
            return entity.getContent();
         }
         catch(IOException e) {
            return null;
         }
      }

      public String getHeader(String name) {
         return headers.get(name);
      }

      public long getContentLength() {
         return entity.getContentLength();
      }
   }

   public interface AuthenticationInfo {
      boolean isLogin();

      void login();

      Object getLoginInfo();

      void setLoginInfo(Object info);
   }

   private static AuthenticationInfo info;
   private CloseableHttpClient client;
   private static List<RequestHandler> handlers;
}


