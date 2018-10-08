package com.yh.request;

import com.yh.common.request.Params;
import com.yh.common.request.RequestHandler;
import com.yh.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReviewBoardRequestHandler extends RequestHandler {

   public ReviewBoardRequestHandler() {
      setDefaultAuthenticationInfo(new ReviewBoardAuthenticationInfo());
   }

   public void login(final String username, final String password) {
      try {
         get("http://192.168.1.134:81/account/login/", new ResponseHandler() {
            @Override
            public void handle(Response response) throws Exception {
               if(response.getStatusCode() == 200) {
                  String text = StringUtil.toString(response.getContent());
                  Document doc = Jsoup.parse(text);
                  Elements is = doc.select("input[name=csrfmiddlewaretoken]");

                  if(is != null) {
                     final String token = is.get(0).val();
                     post("http://192.168.1.134:81/account/login/",
                        Params.get().setParam("username", username).setParam("password", password)
                           .setParam("csrfmiddlewaretoken", token), new ResponseHandler() {
                           @Override
                           public void handle(Response response) throws Exception {
                              if(response.getStatusCode() == 200) {
                                 String text = StringUtil.toString(response.getContent());
                                 Document doc = Jsoup.parse(text);
                                 if(doc.getElementById("id_username") != null && doc
                                    .getElementById("id_password") != null)
                                 {
                                    System.out.println("登录失败");
                                 }
                              }
                              else if(response.getStatusCode() == 302) {
                                 String location = response.getHeader("Location");

                                 if(location.equals("http://192.168.1.134:81/dashboard/")) {
                                    System.out.println("登录成功");
                                 }
                              }
                           }
                        });
                  }
               }

            }
         });
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public int getId() {
      return 2;
   }

   public static class ReviewBoardAuthenticationInfo implements AuthenticationInfo {

      public boolean isLogin() {
         return login;
      }

      public void login() {
         this.login = true;
      }

      public Object getLoginInfo() {
         return username;
      }

      public void setLoginInfo(Object info) {
         this.username = info.toString();
      }

      private boolean login;
      private String username;
   }
}
