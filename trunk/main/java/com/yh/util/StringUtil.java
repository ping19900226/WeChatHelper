package com.yh.util;

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
}
