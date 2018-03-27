package com.yh.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class RDCapture extends Capture{
   public void grap() {
      String content = get("http://pgs.ruc.edu.cn/categoryWeb-category.id=30.htm");
      Document doc = Jsoup.parse(content);
      Element detailItems = doc.getElementById("detail_items");
      Elements subItems = detailItems.select(".sub_items");
      boolean canSearch = false;

      for(Element subItem : subItems) {
         Element a = subItem.select("a").first();
         System.out.println(a.text());

         if(a.text().equals("2018年博士生初试成绩查询") ||
            a.text().startsWith("2018年博士生初试成绩查询") ||
            a.text().contains("2018年博士生初试成绩查询")) {
            canSearch = true;
            break;
         }
      }

      if(canSearch) {
         // send email.
         return;
      }

      try {
         Thread.sleep(1000 * 60 * 60);
      }
      catch(InterruptedException e) {
         e.printStackTrace();
      }
   }

   public List<String> parse(String str) {
      char[] cs = str.toCharArray();

      List<String> rs = new ArrayList<String>();
      String r = "";
      int s = 0;

      for(char c : cs) {
         switch(c) {
            case '$':
               s = 4;
               r += "page[";
               break;
            case '{':
               break;
            case '}':
               r += "]";
               break;
            case '#':
            case '.':
               s = 0;
               r += "select:";
               break;
            case '>':
            case '*':
               String r0 = new String(r);

               if(s == 1) {
                  r0 = "]" + r0;
               }

               r = "";
               s = 0;
               rs.add(r0);
               break;
            case ':':
               s = 2;
               break;
            case '@':
               s = 1;
               r += "attr[";
               break;
            case '(':
               s = 3;
               break;
            case ')':
               r += "]";
               break;
               default:
                  if(s == 2) {
                     r += c;
                  }else if(s == 3) {
                     r += "[";
                     r += c;
                     s = 0;
                  }
                  else {
                     r += c;
                  }

                  break;
         }
      }

      if(s == 1) {
         r += "]";
      }

      rs.add(r);
      return rs;
   }

   public static void main(String[] args) {
      List<String> rs = new RDCapture().parse("${#abc>cde*.ckl:ind(1,2,3)@text}>${#abc>cde*.ckl:ind(1,2,3)@text}");

      for(String r : rs) {
         System.out.println(r);
      }
   }

   class Node {
      private int type;
      private String position;
      private String attr;
   }

   class Pos {
      public static final int TYPE_INDEX = 0;
      public static final int TYPE_EQUALS = 1;
      public static final int TYPE_FIRST = 2;
      public static final int TYPE_LAST= 3;
      private int type;
   }

   class IndexPos {
      private int[] index;
   }

   class EqualsPost {
      private int index;
   }
}
