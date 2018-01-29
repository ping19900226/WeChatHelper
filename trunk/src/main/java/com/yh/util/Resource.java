package com.yh.util;

import java.io.*;
import java.util.Properties;

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
      return "file:" + Resource.class.getClassLoader().getResource("style" + File.separator +
         name).getPath();
   }

   public static String downloadPath() {
      return System.getProperty("user.home") + File.separator + "YH Download";
   }

   public static String documentPath() {
      return System.getProperty("user.home") + File.separator + "YH Data";
   }

   public static Properties loadBugListProperties() {
      String filePath = documentPath().concat(File.separator)
         .concat("conf").concat(File.separator).concat("bug_list.properties");

      File file = new File(filePath);

      Properties prop = new Properties();
      try {

         if(!file.exists()) {
            file.createNewFile();
         }

         prop.load(new FileInputStream(file));
      }
      catch(IOException e) {

      }

      return prop;
   }

   public static Properties loadShareFileProperties() {
      String filePath = documentPath().concat(File.separator)
         .concat("conf").concat(File.separator).concat("share_file.conf");

      File file = new File(filePath);

      Properties prop = new Properties();
      try {

         if(!file.exists()) {
            file.createNewFile();
         }

         prop.load(new FileInputStream(file));
      }
      catch(IOException e) {

      }

      return prop;
   }
}
