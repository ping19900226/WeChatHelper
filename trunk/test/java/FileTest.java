import com.yh.core.YHFileUtil;

import java.util.HashMap;
import java.util.Map;

public class FileTest {

   public static void main(String[] args) {
//      YHFileUtil.copy("F:\\ws\\yh\\src\\main\\java\\com\\yh\\core\\Server.java",
//         "D:\\Server.txt");

      Map<String, String> p = new HashMap<String, String>();
      p.put("java", "txt");
      p.put("xml", "xml");
      YHFileUtil.copyAllFile("F:\\ws\\yh", "D:\\yh", new String[]{"java", "xml"}, p);
   }
}
