import com.yh.core.SSHConnection;

public class SSHConnectionTest {

   public static void main(String [] arg) {
      new SSHConnection().connect("47.91.249.95", "root", "LOVE0226wang", 22);
   }
}
