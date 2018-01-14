package com.yh.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;

public class MD5 {
   /**
    * Digest.
    * @param str
    * @return
    */
   public static String digest(String str) {
      try {
         MessageDigest digest = MessageDigest.getInstance("md5");
         return new String(digest.digest(str.getBytes()));
      }
      catch(Exception e) {
         log.error(e.getMessage(), e);
         return null;
      }
   }

   private static final Log log = LogFactory.getLog(MD5.class);
}
