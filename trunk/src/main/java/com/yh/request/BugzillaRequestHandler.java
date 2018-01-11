package com.yh.request;

import com.yh.util.StringUtil;
import com.yh.request.entity.Bug;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BugzillaRequestHandler extends RequestHandler{
   public BugzillaRequestHandler() {
      setDefaultAuthenticationInfo(new BugzillaAuthenticationInfo());
   }

   public void login(String username, String password) {
      getDefaultAuthenticationInfo().setLoginInfo(username);

      try {
         post("http://192.168.1.120/index.cgi", Params.get()
            .setParam("Bugzilla_login", username)
            .setParam("Bugzilla_password", password)
            .setParam("GoAheadAndLogIn", "Log in"),
            new ResponseHandler() {
            public void handle(Response response) throws Exception {
               if(response.getStatusCode() == 200) {
                  System.out.println("登录成功");
                  getDefaultAuthenticationInfo().login();

                  //System.out.println(StringUtil.toString(response.getContent()));
               }
               else {
                  System.out.println(response.getStatusCode());
               }
            }
         });
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   public List<Bug> getBugList() {
      String username = getDefaultAuthenticationInfo().getLoginInfo().toString();
      StringBuilder url = new StringBuilder();
      url.append("http://192.168.1.120/buglist.cgi?");
      url.append("bug_status=UNCONFIRMED&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED");
      url.append("&bug_status=RESOLVED&emailassigned_to1=1&emailreporter1=1&emailtype1=exact");
      url.append("&email1=" + username);
      url.append("&field0-0-0=bug_status&type0-0-0=notequals&value0-0-0=UNCONFIRMED");
      url.append("&field0-0-1=reporter&type0-0-1=equals");
      url.append("&value0-0-1=" + username);

      final List<Bug> bugs = new ArrayList<Bug>();

      try {
         get(url.toString(), new ResponseHandler() {
            public void handle(Response response) throws Exception {
               if(response.getStatusCode() == 200) {
                  String text = StringUtil.toString(response.getContent());
                  Document doc = Jsoup.parse(text);
                  Element table = doc.select("table.bz_buglist").get(0);
                  Elements trs = table.select("tr.bz_bugitem");

                  for(Element tr : trs) {
                     Elements tds = tr.select("td");
                     Bug bug = new Bug();
                     bug.setId(Integer.parseInt(tds.get(0).text()));
                     bug.setType(tds.get(1).text());
                     bug.setPriority(tds.get(2).text());
                     bug.setReporter(tds.get(3).text());
                     bug.setStatus(tds.get(5).text());
                     bug.setSummary(tds.get(7).text());

                    bugs.add(bug);
                  }

               }
               else {
                  System.out.println(response.getStatusCode());
               }
            }
         });

         return bugs;
      }
      catch(Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static class BugzillaAuthenticationInfo implements AuthenticationInfo {

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
