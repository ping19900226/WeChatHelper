import java.io.*;
import java.nio.charset.Charset;

public class FileWriter {

   private FileWriter(){}

   public FileWriter(String name) throws FileNotFoundException {
      os = new FileOutputStream("D:/a/" + name);
   }

   public void write(String line) throws IOException {
      os.write(line.getBytes(Charset.forName("utf-8")));
      os.write("\n".getBytes());
      count ++;

      if(count > 100) {
         os.flush();
         count = 0;
      }
   }

   public void close() {
      try {
         os.close();
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }

   public int getCount() {
      return count;
   }

   private int count;
   private FileOutputStream os;
}
