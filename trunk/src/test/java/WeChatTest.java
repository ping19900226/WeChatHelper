
import com.yh.request.RequestHandler;
import com.yh.request.WeChatRequestHandler;
import com.yh.view.wx.QRCodeView;
import javafx.application.Application;
import javafx.stage.Stage;

public class WeChatTest extends Application{

   public static void main(String [] arg) {
      launch(arg);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      System.setProperty ("jsse.enableSNIExtension", "false");
      RequestHandler.regist(new WeChatRequestHandler());
      new QRCodeView().start(primaryStage);
   }
}
