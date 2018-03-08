package com.yh.util;

import java.io.*;
import java.nio.charset.Charset;

public class FileUtil {

   public static void writeText(String fileName, String content) {
      try {
         FileOutputStream fos = new FileOutputStream(fileName);

         fos.write(content.getBytes(Charset.forName("utf-8")));

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
}
