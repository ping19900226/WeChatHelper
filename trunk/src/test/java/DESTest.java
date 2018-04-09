import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESTest{
   /**
    * Hex string to byte array.
    * @param hexstr
    * @return
    */
   public static byte[] HexString2Bytes(String hexstr) {
      byte[] b = new byte[hexstr.length() / 2];
      int j = 0;
      for (int i = 0; i < b.length; i++) {
         char c0 = hexstr.charAt(j++);
         char c1 = hexstr.charAt(j++);
         b[i] = (byte) ((parse(c0) << 4) | parse(c1));
      }
      return b;
   }

   /**
    * Encrypt.
    * @param data
    * @param key
    * @return
    */
   public static String encrypt(String data, String key) {
      try {
         Key deskey = keyGenerator(key);
         Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
         SecureRandom random = SecureRandom.getInstance("SHA1PRNG1");;
         cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
         byte[] results = cipher.doFinal(data.getBytes());
         BASE64Encoder base64 = new BASE64Encoder();
         return base64.encode(results);
      }
      catch(Exception e) {
         e.printStackTrace();
         return null;
      }

   }

   /**
    * Decrypt.
    * @param data
    * @param key
    * @return
    */
   public static String decrypt(String data, String key) {
      try {
         Key deskey = keyGenerator(key);
         Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
         cipher.init(Cipher.DECRYPT_MODE, deskey);
         BASE64Decoder base64 = new BASE64Decoder();
         return new String(cipher.doFinal(base64.decodeBuffer(data)));
      }
      catch(Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   private static SecretKey keyGenerator(String keyStr) throws Exception {
      byte input[] = HexString2Bytes(keyStr);
      DESKeySpec desKey = new DESKeySpec(input);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
      SecretKey securekey = keyFactory.generateSecret(desKey);
      return securekey;
   }

   private static int parse(char c) {
      if (c >= 'a') return (c - 'a' + 10) & 0x0f;
      if (c >= 'A') return (c - 'A' + 10) & 0x0f;
      return (c - '0') & 0x0f;
   }

   public static final String KEY_ALGORITHM = "DES";
   public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

   public static void main(String[] args) {
      String key = "32421&^%lksjdfosuadfa";
      String desStr = DESTest.encrypt("abcdefghjfjdasfjkldsjfljdslkfldajs", key);
      System.out.println(desStr);
      System.out.println(DESTest.decrypt(desStr, key));
   }
}
