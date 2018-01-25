package com.yh.util;

import java.io.File;
import java.io.InputStream;

public class Resource {

   public static InputStream loadImage(String name) {
      return Resource.class.getClassLoader().getResourceAsStream("images" + File.separator + name);
   }

   public static String getImagePath(String name) {
      return "file:" + Resource.class.getClassLoader().getResource("images" + File.separator +
         name).getPath();
   }

   public static InputStream loadStyle(String name) {
      return Resource.class.getClassLoader().getResourceAsStream("style" + File.separator + name);
   }

   public static String getStylePath(String name) {
      return Resource.class.getClassLoader().getResource("style" + File.separator + name).getPath();
   }

   public static String downloadPath() {
      return System.getProperty("user.dir") + System.getProperty("file.sp");
   }
}
