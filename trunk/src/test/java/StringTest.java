import com.yh.util.StringUtil;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class StringTest {

   public static void main(String[] args) {
      Font font = new Font(12);
      System.out.println(StringUtil.breakLine(font, "你好这里是你好这里是你好这里是你好这里是", 100));
      System.out.println(StringUtil.breakLine(font, "abcdefghizjkjafdlkjdlaskjflss",
         100));
   }
}
