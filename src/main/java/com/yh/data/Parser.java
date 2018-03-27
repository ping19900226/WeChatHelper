package com.yh.data;

import java.util.ArrayList;
import java.util.List;

public class Parser {
   public List<String> parse(String str) {
      char[] cs = str.toCharArray();

      List<String> rs = new ArrayList<String>();
      String r = "";
      int s = 0;

      for(char c : cs) {
         switch(c) {
            case '$':
               s = 4;
               break;
            case '{':
               if(s == 4) {
                  r += "page[";
               }
               break;
            case '}':
               if(s == 4) {

               }
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
}
