import java.util.Arrays;
import java.util.Collections;

public class SortTest {

   public static void main(String[] args) {
      String[] str = {"abb", "ac", "Ia", "ia", "bg", "B"};

      Collections.sort(Arrays.asList(str));

      System.out.println(Arrays.toString(str));

      System.out.println(System.getProperty("user.home"));
      System.out.println(System.getProperty("user.dir"));
      System.out.println(System.getProperty("path.separator"));
      System.out.println(System.getProperty("file.separator"));

      System.out.println(Arrays.toString("1bc|df|df".split("\\|")));
   }
}
