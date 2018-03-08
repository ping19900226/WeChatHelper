import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;
import com.sun.jna.platform.FileUtils;
import com.yh.document.PDFRenderListener;
import com.yh.util.FileUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class TestPDF {
   private BaseColor lastColor;
   private DocumentFont lastFont;
   private String lastStr = "";

   public void readPDF() {
      Document doc = new Document();

      try {
         PdfReader reader = new PdfReader("F:/1.pdf");
         int pages = reader.getNumberOfPages();
         StringBuilder content = new StringBuilder();
         for(int i = 1; i <= pages; i++) {
            //PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            //parser.processContent(i, new PDFRenderListener());
            content.append(PdfTextExtractor.getTextFromPage(reader, i));

         }

         FileUtil.writeText("F:/1.txt", content.toString().replace("\n", "\r\n"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }

   }

   public static void main(String[] args) {
      new TestPDF().readPDF();
   }
}
