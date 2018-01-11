import com.yh.request.BugzillaRequestHandler;

public class BugzillaRequestTest {

   public static void main(String[] args) {
      BugzillaRequestHandler factory = new BugzillaRequestHandler();
      factory.login("yihuanwang@yonghongtech.com", "yihuan");
      factory.getBugList();
   }
}
