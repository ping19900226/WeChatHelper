package com.yh.request.entity;

import java.util.List;

public class DataGrid<T> {

   public List<String> getHeaders() {
      return headers;
   }

   public void setHeaders(List<String> headers) {
      this.headers = headers;
      this.hSize = headers.size();
   }

   public List<T> getDatas() {
      return datas;
   }

   public void setDatas(List<T> datas) {
      this.datas = datas;
   }

   public int headerSize() {
      return hSize;
   }

   private List<String> headers;
   private List<T> datas;
   private int hSize;
}
