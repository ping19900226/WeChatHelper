
import com.yh.view.SSHView;
import javafx.application.Application;
import javafx.stage.Stage;

public class SSHConnectionTest extends Application{

   public static void main(String [] arg) {
      launch(arg);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      new SSHView().start(primaryStage);
   }
}
