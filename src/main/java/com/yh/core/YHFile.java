package com.yh.core;

public class YHFile {

   public static final int TYPE_DIRECTORY = 1;
   public static final int TYPE_FILE = 2;

   private String name;
   private String path;
   private String parentPath;
   private int createTime;
   private int type;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getParentPath() {
      return parentPath;
   }

   public void setParentPath(String parentPath) {
      this.parentPath = parentPath;
   }

   public int getCreateTime() {
      return createTime;
   }

   public void setCreateTime(int createTime) {
      this.createTime = createTime;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
