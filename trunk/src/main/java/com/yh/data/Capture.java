package com.yh.data;

import com.yh.request.RequestHandler;
import com.yh.util.StringUtil;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.util.*;

public abstract class Capture {

   public abstract void grap();

   public String get(String url) {
      try {
         return get0(url);
      }
      catch(Exception e) {
         System.err.println(e);
         return null;
      }
   }

   public String post(String url, Map<String, String> params) {
      try {
         List<NameValuePair> nvps = new ArrayList<NameValuePair>();

         for(Map.Entry<String, String> entry : params.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
         }

         return post0(url, new UrlEncodedFormEntity(nvps));
      }
      catch(Exception e) {
         System.err.println(e);
         return null;
      }
   }

   private String post0(String url, HttpEntity entity) throws Exception {
      HttpPost httpPost = new HttpPost(url);

      httpPost.setEntity(entity);
      CloseableHttpResponse response = client.execute(httpPost);

      try {
         StatusLine status = response.getStatusLine();
         HttpEntity httpEntity = response.getEntity();
         String result = StringUtil.toString(httpEntity.getContent());
         EntityUtils.consume(entity);
         return result;
      }
      finally {
         response.close();
      }
   }

   private String get0(String url) throws Exception {
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response = client.execute(httpGet);

      try{
         HttpEntity httpEntity = response.getEntity();
         String result = StringUtil.toString(httpEntity.getContent());
         EntityUtils.consume(httpEntity);
         return result;
      }
      finally {
         response.close();
      }
   }

   private CloseableHttpClient client = HttpClients.createDefault();
}
