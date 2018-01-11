import java.util.Arrays;
import java.util.Collections;

public class SortTest {

   public static void main(String[] args) {
      String[] str = {"abb", "ac", "Ia", "ia", "bg", "B"};

      Collections.sort(Arrays.asList(str));

      System.out.println(Arrays.toString(str));
   }
}
