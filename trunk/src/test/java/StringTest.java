import java.io.*;

public class StringTest {

   public static void main(String[] args) {
//      Font font = new Font(12);
//      System.out.println(StringUtil.breakLine(font, "你好这里是你好这里是你好这里是你好这里是", 100));
//      System.out.println(StringUtil.breakLine(font, "abcdefghizjkjafdlkjdlaskjflss",
//         100));
      String str = "a\\\\$a$a%!@#\\$%^&*()~`:{}|[];'<>?,./";
      str = readFile();
      System.out.println("from file: " + str);
      //str = str.replace("\\", "\\\\");
      //str = str.replace("$", "\\$");
      //str = replace0(str);
      //writeFile(str);

      System.out.println(replaceFirst("bbb ?aaaaa", "?", str));
   }

   private static void writeFile(String str) {
      File file = new File("D:/r.txt");

      try {
         FileOutputStream fos = new FileOutputStream(file);
         fos.write(str.getBytes());
         fos.flush();
         fos.close();
      }
      catch(FileNotFoundException e) {
         e.printStackTrace();
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }

   private static String readFile() {
      File file = new File("D:/r.txt");
      StringBuilder sb = new StringBuilder();

      try {
         FileInputStream fis = new FileInputStream(file);
         byte[] bs = new byte[1024];
         int len = 0;

         while((len = fis.read(bs)) > 0) {
            sb.append(new String(bs, 0, len));
         }

         fis.close();
      }
      catch(FileNotFoundException e) {
         e.printStackTrace();
      }
      catch(IOException e) {
         e.printStackTrace();
      }

      return sb.toString();
   }

   private static String replace0(String str) {
      int ind = -1;
      StringBuilder cs = new StringBuilder();
      if(str.indexOf("$") >= 0) {
         char[] vs = str.toCharArray();


         for(int i = 0; i < vs.length; i++) {
            if(vs[i] == '\\') {
               ind = ind > 0 ? ind :  i;
            }
            else {
               if(vs[i] == '$') {
                  if(ind == -1 || (i - ind) % 2 == 0) {
                     cs.append('\\');
                     ind = -1;
                  }
               }
               else {
                  ind = ind > 0 ? -1 : ind;
               }
            }

            cs.append(vs[i]);
         }

         return cs.toString();
      }

      return str;
   }

   private static String replace1(String str) {
      if(str.indexOf("$") >= 0) {
         return str.replace("$", "\\$");
      }

      return str;
   }

   private static String replaceFirst(String str, String oldChar, String newChar) {
      int qmIdx;

      if((qmIdx = str.indexOf(oldChar)) >= 0) {
         char[] c = str.toCharArray();
         return new StringBuilder(new String(c, 0, qmIdx)).append(newChar)
            .append(new String(c, qmIdx + oldChar.length(), c.length - qmIdx - oldChar.length()
            )).toString();
      }

      return str;
   }
}
