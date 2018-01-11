package com.yh.request;

import javax.activation.MimeType;
import java.io.IOException;
import java.net.*;

public class YHContentHandlerFactory implements ContentHandlerFactory {
   @Override
   public ContentHandler createContentHandler(String mimetype) {
      System.out.println("MIME_TYPE: " + mimetype);
      return new YHContentHandler();
   }

   class YHContentHandler extends ContentHandler {

      @Override
      public Object getContent(URLConnection urlc) throws IOException {
         System.out.println(urlc.getContentType());
         return null;
      }
   }
}
