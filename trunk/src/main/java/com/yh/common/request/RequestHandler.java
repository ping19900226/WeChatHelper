package com.yh.common.request;

import com.yh.common.data.FinalData;
import com.yh.util.StringUtil;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

public abstract class RequestHandler {

   {
      client = RequestHelper.getHttpClient();
   }

   public static RequestHandler getRequestHandler(int id) {
      for(RequestHandler handler : handlers) {
         if(handler.getId() == id) {
            return handler;
         }
      }

      return null;
   }

   public static void regist(RequestHandler handler) {
      handlers.add(handler);
   }

   public void setDefaultAuthenticationInfo(AuthenticationInfo info) {
      this.info = info;
   }

   public AuthenticationInfo getDefaultAuthenticationInfo() {
      return info;
   }

   public abstract void login(String username, String password);

   public abstract int getId();

   protected void get(String url, ResponseHandler handler) throws Exception {
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response = client.execute(httpGet);

      try{
         StatusLine status = response.getStatusLine();
         HttpEntity entity = response.getEntity();

         if(handler != null) {
            handler.handle(new Response(status, entity, response.getAllHeaders()));
         }

         EntityUtils.consume(entity);
      }
      finally {
         response.close();
      }
   }

   public String getContent(String url) {
      final FinalData<String> data = new FinalData<String>();

      try {
         get(url, new ResponseHandler() {
            @Override
            public void handle(Response response) throws Exception {
               data.setValue(response.getStringContent());
            }
         });

         return data.getValue();
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public String postContent(String url, Params params) {
      return postContent0(url, params);
   }

   public String postContent(String url, String content) {
      return postContent0(url, content);
   }

   private String postContent0(String url, Object param) {
      final FinalData<String> data = new FinalData<String>();

      try {
         if(param instanceof Params) {
            post(url, (Params) param, new ResponseHandler() {
               @Override
               public void handle(Response response) throws Exception {
                  data.setValue(response.getStringContent());
               }
            });
         }
         else {
            post(url, param.toString(), new ResponseHandler() {
               @Override
               public void handle(Response response) throws Exception {
                  data.setValue(response.getStringContent());
               }
            });
         }


         return data.getValue();
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public InputStream getInputStream(String url) {
      final FinalData<InputStream> data = new FinalData<InputStream>();

      try {
         get(url, new ResponseHandler() {
            @Override
            public void handle(Response response) throws Exception {
               data.setValue(response.getContent());
            }
         });

         return data.getValue();
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   protected void post(String url, Params param, ResponseHandler handler) throws Exception {
      post0(url, new UrlEncodedFormEntity(param.toNameValuePair()), handler);
   }

   protected void post(String url, String content, ResponseHandler handler) throws Exception {
      post0(url, new StringEntity(content, Charset.forName("UTF-8")), handler);
   }

   private void post0(String url, HttpEntity entity, ResponseHandler handler) throws Exception {
      HttpPost httpPost = new HttpPost(url);

      httpPost.setEntity(entity);
      CloseableHttpResponse response = client.execute(httpPost);

      try {
         StatusLine status = response.getStatusLine();
         HttpEntity httpEntity = response.getEntity();
         // do something useful with the response body
         // and ensure it is fully consumed
         handler.handle(new Response(status, httpEntity, response.getAllHeaders()));
         EntityUtils.consume(entity);
      }
      finally {
         response.close();
      }
   }



   public void upload(String url, String path, ResponseHandler handler) throws Exception {
      HttpPost httppost = new HttpPost(url);

      HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("file", new File(path))
              .addTextBody("id", "")
         .create().addTextBody("name", "")
         .addTextBody("type", "")
         .addTextBody("lastModifiedDate", "")
         .addTextBody("size", "")
         .addTextBody("mediaType", "")
         .addTextBody("uploadmediarequest", "")
         .addTextBody("webwx_data_ticket", "")
         .addTextBody("pass_ticket", "")
         .addTextBody("filename", "")
         .setBoundary("------WebKitFormBoundaryJLAhgXlajXGZB2gI--")
         .build();

      post0(url, entity, handler);

   }

   public String upload(String url, HttpEntity entity) throws Exception {
      final FinalData<String> data = new FinalData<String>();

      post0(url, entity, new ResponseHandler() {
         public void handle(Response response) throws Exception {
            data.setValue(response.getStringContent());
         }
      });

      return data.getValue();
   }

   public String getCookie(String name) {
      List<Cookie> cs = RequestHelper.getCookieStore().getCookies();

      for(Cookie c : cs) {
         if(c.getName().equals(name)) {
            return c.getValue();
         }
      }

      return null;
   }

   protected interface ResponseHandler {

      public void handle(Response response) throws Exception;
   }

    protected class Response {
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

      public String getStringContent() {
         return StringUtil.toString(getContent());
      }
   }

   public interface AuthenticationInfo {
      boolean isLogin();

      void login();

      Object getLoginInfo();

      void setLoginInfo(Object info);
   }

   private AuthenticationInfo info;
   private static List<RequestHandler> handlers = new ArrayList<RequestHandler>();
   private CloseableHttpClient client;
   private static final String HTTP = "http";
   private static final String HTTPS = "https";
}


