package com.yh.qq.request;

import com.yh.common.request.Callback;
import com.yh.common.request.RequestHandler;
import com.yh.common.data.FinalData;
import com.yh.qq.entity.AuthInfo;
import com.yh.util.JSONUtil;
import org.apache.http.HttpStatus;

import java.io.InputStream;

public class QQRequestHandler extends RequestHandler {
   public QQRequestHandler() {
      setDefaultAuthenticationInfo(new QQAuthenticationInfo());
   }
   @Override public void login(String username, String password) {

   }

   private String getLoginUrl() {
      String rs = getContent("http://w.qq.com/js/mq.js?t=20161220");
      rs = rs.replace("\r\n", "");
      rs = rs.substring(rs.indexOf("var LOGIN_URL = '") + "var LOGIN_URL = '".length(),
         rs.length());
      String url = rs.substring(0, rs.indexOf("';"));
      System.out.println("login url: " + url);

      rs = rs.substring(rs.indexOf("var params = ") + "var params = ".length(),
         rs.length());
      rs = rs.substring(0, rs.indexOf(";"));
      System.out.println("login param: " + rs);

      String param = JSONUtil.serialize(rs);
      return url + param;
   }

   public void getQrCode(final Callback<InputStream> callback) {
      AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
      final FinalData<InputStream> is = new FinalData<InputStream>();

      try {
         get("https://ssl.ptlogin2.qq" + ".com/ptqrshow?appid=" + info.getAppid() +
            "&e=2&l=M&s=3&d=72&v=4&t=0.08600492946047433&daid=" + info.getDaid()
            + "&pt_3rd_aid=0", new ResponseHandler() {
            public void handle(Response response) throws Exception {
               if(response.getStatusCode() == HttpStatus.SC_OK) {
                  callback.call(response.getContent());
               }
            }
         });
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   public void checkLogin() {
      AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
      String token = getContent("ptqrtoken");

      String url = "https://ssl.ptlogin2.qq.com/ptqrlogin?u1=http%3A%2F%2Fw.qq.com%2Fproxy.html" +
         "&ptqrtoken=" + token + "&ptredirect=0&h=1&t=1&g=1&from_ui=1&ptlang=2052" +
         "&action=0-0-1516072520493&js_ver=10233&js_type=1" +
         "&login_sig=TSq9PiCRXcuPZsy8CwxhqplRwvuBQ26i0UfLeaZy-XgtBJ2uIu*ruKSGRermVlXc" +
         "&pt_uistyle=40&aid=" + info.getAppid() + "&daid=" + info.getDaid() + "&mibao_css=m_webqq&";


   }

   @Override public int getId() {
      return 0;
   }

   class QQAuthenticationInfo implements AuthenticationInfo {

      public boolean isLogin() {
         return false;
      }

      public void login() {

      }

      public Object getLoginInfo() {
         return info;
      }

      public void setLoginInfo(Object info) {
         this.info = (AuthInfo) info;
      }

      private boolean login;
      private AuthInfo info;
   }
}
