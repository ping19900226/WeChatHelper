package com.yh.util;

import java.io.InputStream;

public class Resource {

   public static InputStream loadImage(String name) {
      return Resource.class.getClassLoader().getResourceAsStream(name);
   }
}
