package com.yh.util;

import javafx.css.Styleable;
import javafx.scene.Node;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

public class Style {

   private List<Attribute> styleList = new ArrayList<Attribute>();

   public static Style parse(String style) {
      return new Style(style);
   }

   public static Style newStyle() {
      return new Style();
   }

   public static void setStyle(Node node, String style) {
      Style style1 = Style.parse(node.getStyle());
      style1.parse0(style);
      node.setStyle(style1.toString());
   }

   public void setStyle(String name, String value) {
      Attribute attr = new Attribute();
      attr.setName(name);
      attr.setValue(value);

      if(styleList.contains(attr)) {
         styleList.remove(attr);
      }

      styleList.add(attr);
   }

   public void setStyle(String style) {
      String[] ss = style.split(":");
      String name = ss[0].trim();
      String value = ss[1].trim();
      setStyle(name, value);
   }

   public String getStyle(String name) {
      for(Attribute attr : styleList) {
         if(attr.getName().equalsIgnoreCase(name)) {
            return attr.getValue();
         }
      }

      return null;
   }


   public void removeStyle(String name) {
      Attribute attr = new Attribute();
      attr.setName(name);

      if(styleList.contains(attr)) {
         styleList.remove(attr);
      }
   }

   private Style() {

   }

   private Style(String style) {
      parse0(style);
   }

   private void parse0(String style) {
      if(style == null || style.trim().isEmpty()) {
         return;
      }

      String[] styles = style.split(";");

      for(String sty : styles) {
         if(sty.trim().isEmpty()) {
            continue;
         }

         String[] ss = sty.split(":");
         String name = ss[0].trim();
         String value = ss[1].trim();
         Attribute attr = new Attribute();
         attr.setName(name);
         attr.setValue(value);

         styleList.add(attr);
      }
   }

   @Override
   public String toString() {
      StringBuilder style = new StringBuilder();

      for(Attribute attr : styleList) {
         style.append(attr.getName() + ": " + attr.getValue() + ";");
      }

      return style.toString();
   }

   class Attribute {
      private String name;
      private String value;

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getValue() {
         return value;
      }

      public void setValue(String value) {
         this.value = value;
      }

      @Override public boolean equals(Object o) {
         if(this == o) {
            return true;
         }
         if(o == null || getClass() != o.getClass()) {
            return false;
         }

         Attribute attribute = (Attribute) o;

         return name.equalsIgnoreCase(attribute.name);
      }

      @Override public int hashCode() {
         return name.hashCode();
      }
   }
}
