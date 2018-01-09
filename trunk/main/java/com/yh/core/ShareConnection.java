package com.yh.core;

import jcifs.smb.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.*;

public class ShareConnection {

   private String cdomain;
   private String domain;

   public ShareConnection(String domain, String username, String password) {
      this.cdomain = "sbm://" + username + ":" + password + "@" + domain + "/";
      this.domain = domain;

      final NtlmPasswordAuthentication auth =  new NtlmPasswordAuthentication(domain, username,
         password);
      NtlmAuthenticator.setDefault(new NtlmAuthenticator() {
         @Override protected NtlmPasswordAuthentication getNtlmPasswordAuthentication() {
            return auth;
         }
      });

   }

   public List<YHFile> listAll(YHFile parent) throws IOException {
      SmbFile sf = parent == null ? new SmbFile("sbm://" + domain) : parent.getSmbFile();
      return listAll(sf, null);
   }

   public List<YHFile> listChild() throws SmbException, MalformedURLException {
      return listChild(null);
   }

   public List<YHFile> listChild(YHFile file) throws SmbException, MalformedURLException {
      SmbFile sf = file == null || file.isEmpty() ? new SmbFile("sbm://" + domain) : file.getSmbFile();
      if(sf.isDirectory()) {
         List<SmbFile> files = Arrays.asList(sf.listFiles());
         List<YHFile> fs = new ArrayList<YHFile>();

         for(SmbFile f : files) {
            fs.add(YHFileUtil.toYHFile(f));
         }

         return fs;
      }

      List<YHFile> fs = new ArrayList<YHFile>();
      fs.add(file);
      return fs;
   }

   public List<YHFile> listAll(SmbFile file, List<YHFile> list) {
      list = list == null ? new ArrayList<YHFile>() : list;

      try {
         if(file.isDirectory()) {
            SmbFile[] fs = file.listFiles();

            for(SmbFile f : fs) {
               listAll(f, list);
            }
         }
         else {
            list.add(YHFileUtil.toYHFile(file));
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

   public InputStream loadFile(YHFile file) throws IOException {
      return file.getSmbFile().getInputStream();
   }
}
