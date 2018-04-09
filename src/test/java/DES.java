import java.security.SecureRandom;
import javax.crypto.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES
 */
public class DES {
   /**
    * Encryption plaintext input.
    */
   public static String encryptStr(String str, String key) {
      BASE64Encoder base64en = new BASE64Encoder();
      try {
         byte[] byteMing = str.getBytes("UTF8");
         byte[] byteMi = encryptByte(byteMing, generteKey(key));
         return base64en.encode(byteMi);
      } catch (Exception e) {
         throw new RuntimeException(
            "Error initializing SqlMap class. Cause: " + e);
      }
   }

   /**
    * Decrypt cipher text.
    */
   public static String decryptStr(String encryptStr, String key) {
      BASE64Decoder base64De = new BASE64Decoder();
      try {
         byte[] byteMi = base64De.decodeBuffer(encryptStr);
         byte[] byteMing = decryptByte(byteMi, generteKey(key));
         return new String(byteMing, "UTF8");
      } catch (Exception e) {
         throw new RuntimeException(
            "Error initializing SqlMap class. Cause: " + e);
      }
   }

   private DES() {

   }

   private static SecretKey generteKey (String key) {
      try {
         KeyGenerator _generator = KeyGenerator.getInstance("DES");
         //Prevent random generation of keys under Linux.
         SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
         secureRandom.setSeed(key.getBytes());

         _generator.init(56,secureRandom);
         return _generator.generateKey();
      } catch (Exception e) {
         throw new RuntimeException(
            "Error initializing SqlMap class. Cause: " + e);
      }
   }

   private static byte[] encryptByte(byte[] byteS, SecretKey key) {
      try {
         Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, key);
         return cipher.doFinal(byteS);
      } catch (Exception e) {
         throw new RuntimeException(
            "Error initializing SqlMap class. Cause: " + e);
      }
   }

   private static byte[] decryptByte(byte[] byteD, SecretKey key) {
      try {
         Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
         cipher.init(Cipher.DECRYPT_MODE, key);
         return cipher.doFinal(byteD);
      } catch (Exception e) {
         throw new RuntimeException(
            "Error initializing SqlMap class. Cause: " + e);
      }
   }

   public static void main(String[] args) throws Exception {
      String str1 = "1111111111111111";
      String str2 = DES.encryptStr(str1, "dhflaskjfiwrufwn");
      String deStr = DES.decryptStr(str2, "dhflaskjfiwrufwn");
      System.out.println(" before encrypt： " + str1);
      System.out.println(" after encrypt： " + str2);
      System.out.println(" after encrypt length： " + str2.length());
      System.out.println(" after decrypt： " + deStr);
   }
}
