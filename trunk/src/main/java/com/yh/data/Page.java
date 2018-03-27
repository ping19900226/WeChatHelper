package com.yh.data;

import java.util.List;

public class Page {
   private String url;
   private List<Node> nodes;

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public List<Node> getNodes() {
      return nodes;
   }

   public void setNodes(List<Node> nodes) {
      this.nodes = nodes;
   }
}
