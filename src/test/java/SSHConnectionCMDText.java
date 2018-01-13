
import com.yh.core.SSHConnection;
import com.yh.view.SSHView;
import javafx.application.Application;
import javafx.stage.Stage;

public class SSHConnectionCMDText{
  public static void main(String[] args) {
     SSHConnection conn = new SSHConnection();
     conn.connect("47.91.249.95", "root", "LOVE0226wang", 22);
     conn.execute(System.in, System.out);
  }
}
