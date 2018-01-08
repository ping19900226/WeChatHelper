package com.yh.core;

import jcifs.smb.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.*;

public class Connection {

   private String url;
   private String cdomain;

   public Connection(String domain, String username, String password, String path) {
      this.cdomain = "sbm://" + username + ":" + password + "@" + domain + "/";
      this.url = cdomain + path;

   }

   public List<SmbFile> listAll(String path) throws IOException {
      path = path == null ? "" : path;
      path = path.startsWith("sbm://") ? path : cdomain + path;
      SmbFile file = new SmbFile(path);
      file.connect();
      return listAll(file, null);
   }

   public List<SmbFile> listChild(SmbFile file) throws SmbException {
      if(file.isDirectory()) {
         return Arrays.asList(file.listFiles());
      }

      List<SmbFile> fs = new ArrayList<SmbFile>();
      fs.add(file);
      return fs;
   }

   public List<SmbFile> listChild(String path) throws IOException {
      path = path == null ? "" : path;
      path = path.startsWith("sbm://") ? path : cdomain + path;
      SmbFile file = new SmbFile(path);
      file.connect();
      return listChild(file);
   }

   public List<SmbFile> listAll(SmbFile file, List<SmbFile> list) {
      System.out.println(file.getURL().toString() + "------" + file.getShare() + "----" + file
         .getCanonicalPath());
      list = list == null ? new ArrayList<SmbFile>() : list;

      try {
         if(file.isDirectory()) {
            SmbFile[] fs = file.listFiles();

            for(SmbFile f : fs) {
               listAll(f, list);
            }
         }
         else {
            list.add(file);
         }
      }
      catch(SmbException e) {
         if(e instanceof  SmbAuthException) {
            e.printStackTrace();
            System.out.println("账号密码错误");
         }
         else {
            System.out.println("错误");
         }
      }
//      catch(MalformedURLException e) {
//         System.out.println("地址错误");
//      }

      return list;
   }

   public InputStream loadFile(String path) throws IOException {
      path = path == null ? "" : path;
      path = path.startsWith("sbm://") ? path : cdomain + path;
      SmbFile file = new SmbFile(path);
      file.connect();
      return loadFile(file);
   }

   public InputStream loadFile(SmbFile file) throws IOException {
      return file.getInputStream();
   }

   public static void main(String[] args) {
      try {
         new Connection("192.168.1.134", "root", "forDEV123", "")
            .listAll("");
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
}
