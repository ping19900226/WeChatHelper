package com.yh.core;

import jcifs.smb.SmbFile;

import java.net.URL;

public class YHFile {

   public static final int TYPE_DIRECTORY = 1;
   public static final int TYPE_FILE = 2;

   private String name;
   private String path;
   private String parentPath;
   private long createTime;
   private long modifyTime;
   private int type;
   private int size;
   private boolean directory;
   private boolean file;
   private URL URL;
   private SmbFile smbFile;
   private boolean empty;

   public YHFile() {
      this.empty = false;
   }

   /**
    * Create a file that contains nothing.
    */
   public YHFile(boolean empty) {
      this.empty = empty;
   }

   public static YHFile emptyFile() {
     return new YHFile(true);
   }

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

   public long getCreateTime() {
      return createTime;
   }

   public void setCreateTime(long createTime) {
      this.createTime = createTime;
   }

   public long getModifyTime() {
      return modifyTime;
   }

   public void setModifyTime(long modifyTime) {
      this.modifyTime = modifyTime;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getSize() {
      return size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public boolean isDirectory() {
      return directory;
   }

   public void setDirectory(boolean directory) {
      this.directory = directory;
   }

   public boolean isFile() {
      return file;
   }

   public void setFile(boolean file) {
      this.file = file;
   }

   public URL getURL() {
      return URL;
   }

   public void setURL(URL URL) {
      this.URL = URL;
   }
      public boolean isEmpty() {
      return empty;
   }

   SmbFile getSmbFile() {
      return smbFile;
   }

   void setSmbFile(SmbFile smbFile) {
      this.smbFile = smbFile;
   }
}
