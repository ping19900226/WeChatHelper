package com.yh.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JDCapture extends Capture{
   public void grap() {
      String result =  get("https://search.jd" +
         ".com/Search?keyword=%E7%AC%94%E8%AE%B0%E6%9C%AC%E7%94%B5%E8%84" +
         "%91%20%E4%BA%AC%E4%B8%9C%E8%87%AA%E8%90%A5&enc=utf-8&wq=%E7%AC%94%E8%AE%B0%E6%9C%AC%E7%94%B5%E8%84%91%20%E4%BA%AC%E4%B8%9C%E8%87%AA%E8%90%A5&pvid=4cf91e35870e44188ec59e86fd583a7c");


      Document doc = Jsoup.parse(result);
      Element ele = doc.getElementById("J_goodList");
   }

   public static void main(String[] args) {
      new JDCapture().grap();
   }

   private void store() {

   }

   private void createTable() {
      DatabaseUtil.get().execute("" +
         "CREATE TABLE goods (" +
         ")");
   }
}
