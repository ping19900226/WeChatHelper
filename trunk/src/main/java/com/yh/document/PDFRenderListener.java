package com.yh.document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.parser.*;

public class PDFRenderListener implements RenderListener{
   private BaseColor lastColor;
   private DocumentFont lastFont;
   private String lastStr = "";

   @Override public void beginTextBlock() {

   }

   @Override public void renderText(TextRenderInfo renderInfo) {
      if(lastFont != null && lastColor != null &&
         lastFont.getPostscriptFontName().equals(renderInfo.getFont().getPostscriptFontName()) &&
         lastColor.getRed() == renderInfo.getFillColor().getRed()) {
         lastStr += renderInfo.getText();
      }
      else {
         lastStr += renderInfo.getText();
         System.out.println("<span " + (lastColor == null ? "" : lastColor.getRGB()) +
            " " + (lastFont == null ? "" : lastFont.getPostscriptFontName()) + ">" +
            lastStr +
            "</span>");
         lastStr = "";
      }

      lastColor = renderInfo.getFillColor();
      lastFont = renderInfo.getFont();
   }

   @Override public void endTextBlock() {

   }

   @Override public void renderImage(ImageRenderInfo renderInfo) {

   }
}
