package com.yh.request;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class Params {

   private Map<String, String> params = new HashMap<String, String>();

   public static Params get() {
      return new Params();
   }

   public Params setParam(String name, String value) {
      params.put(name, value);
      return this;
   }

   public String toString() {
      StringBuilder res = new StringBuilder();

      for(Map.Entry<String, String> entry : params.entrySet()) {
         res.append(entry.getKey() + "=" + entry.getValue()).append("&");
      }

      return res.toString();
   }

   public List<NameValuePair> toNameValuePair() {
      List <NameValuePair> nvps = new ArrayList<NameValuePair>();

      for(Map.Entry<String, String> entry : params.entrySet()) {
         nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
      }

      return nvps;
   }
}
