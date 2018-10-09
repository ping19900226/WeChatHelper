import java.util.Arrays;
import java.util.Collections;

public class SortTest {

   public static void main(String[] args) {
      String name = "<span class=\"emoji emoji1f4b0\"></span>万元户<span class=\"emoji emoji1f3e1\"></span>";

      while(name.contains("<span class=\"emoji")) {
         int st = name.indexOf("<span class=\"emoji");
         int et = name.indexOf("</span>", st);

         String name0 = name.substring(0, st);

         if(st >= 0) {
            name0 += "口";
         }

         name = name0 + (name.substring(et + 7, name.length()));
      }

      System.out.println(name);
   }
}
