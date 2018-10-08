import com.yh.core.YHFileUtil;

import java.util.*;

public class FileTest {

   public static void main(String[] args) {
//      YHFileUtil.copy("F:\\ws\\yh\\src\\main\\java\\com\\yh\\core\\Server.java",
//         "D:\\Server.txt");

      Map<String, String> p = new HashMap<String, String>();
      p.put("java", "javax");
      p.put("xml", "xmls");
      YHFileUtil.copyAllFile("F:\\ws\\bi_8_0\\mixUp", "D:\\bi_8_0_txt\\mixUp", null, p, Arrays
         .asList
         (new String[]{".svn", "bihome", "node_modules", "js", ".idea"}));
   }
}
