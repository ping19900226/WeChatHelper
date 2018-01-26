package com.yh.util;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.text.Font;

import java.io.*;

public class StringUtil {

   public static String toString(InputStream is) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String line = "";
      StringBuilder res = new StringBuilder();

      try {
         while((line = reader.readLine()) != null) {
            res.append(line).append("\n");
         }

         return res.toString();
      }
      catch(IOException e) {
         return null;
      }
      finally {
         try {
            reader.close();
         }
         catch(IOException e) {
            // ignore
         }
      }
   }

   public static String breakLine(Font font, String str, int width) {
      double size = font.getSize();
      double tSize = str.length() * size;
      int num = (int) (width/size);
      StringBuilder result = new StringBuilder();

      if(tSize > width) {
         result.append(str.substring(0, num - 1)).append("\n");
         result.append(str.substring(num - 1, str.length()));
      }
      else {
         result.append(str);
      }

      return result.toString();
   }
}
