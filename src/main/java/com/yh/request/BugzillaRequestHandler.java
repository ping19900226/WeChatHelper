package com.yh.request;

import com.yh.request.entity.DataGrid;
import com.yh.util.StringUtil;
import com.yh.request.entity.Bug;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

   @Override
   public int getId() {
      return 1;
   }

   public DataGrid<List<String>> getBugList() {
      String username = getDefaultAuthenticationInfo().getLoginInfo().toString();
      StringBuilder url = new StringBuilder();
      url.append("http://192.168.1.120/buglist.cgi?");
      url.append("bug_status=UNCONFIRMED&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED");
      url.append("&bug_status=RESOLVED&emailassigned_to1=1&emailreporter1=1&emailtype1=exact");
      url.append("&email1=" + username);
      url.append("&field0-0-0=bug_status&type0-0-0=notequals&value0-0-0=UNCONFIRMED");
      url.append("&field0-0-1=reporter&type0-0-1=equals");
      url.append("&value0-0-1=" + username);
      url.append("&columnlist=all");

      final DataGrid<List<String>> data = new DataGrid<List<String>>();

      try {
         get(url.toString(), new ResponseHandler() {
            public void handle(Response response) throws Exception {
               if(response.getStatusCode() == 200) {
                  String text = StringUtil.toString(response.getContent());
                  Document doc = Jsoup.parse(text);
                  Element table = doc.select("table.bz_buglist").get(0);
                  Element hr = table.select("tr.bz_buglist_header").get(0);
                  Elements hcs = hr.select("th");
                  List<String> headers = new ArrayList<String>();
                  data.setHeaders(headers);

                  for(Element hc : hcs) {
                     headers.add(hc.text());
                  }

                  Elements trs = table.select("tr.bz_bugitem");
                  List<List<String>> bugs = new ArrayList<List<String>>();
                  data.setDatas(bugs);

                  for(Element tr : trs) {
                     Elements tds = tr.select("td");
                     List<String> bcs = new ArrayList<String>();
                     bugs.add(bcs);

                     for(Element td : tds) {
                        bcs.add(td.text());
                     }
                  }

               }
               else {
                  System.out.println(response.getStatusCode());
               }
            }
         });

         return data;
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
