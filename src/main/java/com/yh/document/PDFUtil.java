package com.yh.document;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.io.IOException;

public class PDFUtil {

   public void readPDF(String filePath) {
      try {
         PdfReader reader = new PdfReader(filePath);
         int pages = reader.getNumberOfPages();
         for(int i = 1; i <= pages; i++) {
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            parser.processContent(i, new RenderListener() {
               @Override public void beginTextBlock() {

               }

               @Override public void renderText(TextRenderInfo renderInfo) {
                  System.out.println(renderInfo.getText());
                  System.out.println(renderInfo.getFillColor());
                  System.out.println(renderInfo.getFont());
                  System.out.println(renderInfo.getPdfString());
               }

               @Override public void endTextBlock() {
                  System.out.println();
               }

               @Override public void renderImage(ImageRenderInfo renderInfo) {

               }
            });

            System.out.println(PdfTextExtractor.getTextFromPage(reader, i));
         }
      }
      catch(IOException e) {
         e.printStackTrace();
      }

   }
}
