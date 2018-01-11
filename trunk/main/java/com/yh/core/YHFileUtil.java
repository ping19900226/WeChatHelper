package com.yh.core;

import javafx.scene.control.Alert;
import jcifs.smb.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class YHFileUtil {

   public static boolean downloadShareFile(String dir, YHFile file, ShareConnection conn) {
      // 数组过大竟然会引起文件损坏，损坏原因是byte未填满，自动补0的问题
      SmbFileInputStream is = null;

     try {
         is = (SmbFileInputStream) conn.loadFile(file);
         downloadFile(dir, is, file.getName(), null);
         return true;
      }
      catch(IOException e) {
         e.printStackTrace();
         log.error(e);
         return false;
      }
   }

   public static boolean downloadFile(String dir, InputStream is, String fileName,
      DownloadListener listener)
   {
      // 数组过大竟然会引起文件损坏，损坏原因是byte未填满，自动补0的问题
      FileOutputStream fos = null;

      try {
         fos = new FileOutputStream(dir + "/" + fileName);
         int count = 0;
         int read = 0;
         byte[] bs = new byte[10 * 1024];

         while((count = is.read(bs)) > 0) {
            fos.write(bs, 0, count);
            read += count;
            listener.download(read);
         }

         fos.flush();
         return true;
      }
      catch(IOException e) {
         log.error(e);
         return false;
      }
      finally {
         if(is != null) {
            try {
               is.close();
            }
            catch(IOException e) {
               // ignore
            }
         }

         if(fos != null) {
            try {
               fos.close();
            }
            catch(IOException e) {
               // ignore
            }
         }
      }
   }

   public static YHFile toYHFile(SmbFile file) {
      YHFile f = new YHFile();
      f.setName(file.getName());
      f.setParentPath(file.getParent());
      f.setPath(file.getPath());
      f.setModifyTime(file.getLastModified());
      f.setSize(file.getContentLength());
      f.setURL(file.getURL());
      f.setSmbFile(file);

      try {
         f.setDirectory(file.isDirectory());
         f.setFile(file.isFile());
         f.setCreateTime(file.createTime());
      }
      catch(SmbException e) {
         log.error(e);
      }

      return f;
   }

   public static void copy(String source, String target) {
      File outFile = new File(target);

      target = target.replace("\\", "/");
      String fileName = target.substring(target.lastIndexOf("/") + 1, target.length());
      String dir = target.substring(0, target.lastIndexOf("/"));
      File dirFile = new File(dir);

      try {
         if(!outFile.exists()) {
            dirFile.mkdirs();
            outFile.createNewFile();
         }

         copy(new File(source), outFile);
      }
      catch(FileNotFoundException e) {
         log.error(e);
      }
      catch(IOException e) {
         log.error(e);
      }
   }

   private static void copy(File source, File target) throws IOException {
      FileInputStream fis = null;
      FileOutputStream fos = null;

      try {
         fis = new FileInputStream(source);
         fos = new FileOutputStream(target);

         byte[] bs = new byte[10 * 1024];
         int len = 0;

         while((len = fis.read(bs)) > 0) {
            fos.write(bs, 0, len);
         }

         fos.flush();
      }
      finally {
         if(fis != null){
            try {
               fis.close();
            }
            catch(IOException e) {
               // ignore
            }
         }

         if(fos != null) {
            try {
               fos.close();
            }
            catch(IOException e) {
               // ignore
            }
         }
      }
   }

   public static void copyAllFile(String sourceDir, String targetDir)
   {
      File sdf = new File(sourceDir);
      copyAllFile(sdf, sourceDir, targetDir, null, null, null, null);
   }

   public static void copyAllFile(String sourceDir, String targetDir, String[] fileTypes)
   {
      File sdf = new File(sourceDir);
      copyAllFile(sdf, sourceDir, targetDir, Arrays.asList(fileTypes), null, null, null);
   }

   public static void copyAllFile(String sourceDir, String targetDir, String[] fileTypes,
      String type)
   {
      File sdf = new File(sourceDir);
      copyAllFile(sdf, sourceDir, targetDir, fileTypes == null ? null : Arrays.asList(fileTypes),
         type, null, null);
   }

   public static void copyAllFile(String sourceDir, String targetDir, String[] fileTypes,
      Map<String, String> type)
   {
      File sdf = new File(sourceDir);
      copyAllFile(sdf, sourceDir, targetDir, fileTypes == null ? null : Arrays.asList(fileTypes),
         type, null, null);
   }

   private static void copyAllFile(File file, String sourceDir, String targetDir, List<String>
      fileTypes, Object type, List<String> ignoreDir, List<String> ignoreFile)
   {
      try {
         copyAllFile0(file, sourceDir, targetDir, fileTypes, type, ignoreDir, ignoreFile);
      }
      catch(IOException e) {
         log.error(e);
      }
   }

   private static void copyAllFile0(File file, String sourceDir, String targetDir, List<String>
      fileTypes, Object types, List<String> ignoreDir, List<String> ignoreFile) throws IOException
   {
      if(file.isDirectory()) {
         if(!ignoreDir.contains(file.getName())) {
            File[] files = file.listFiles();
            for(File f : files) {
               copyAllFile0(f, sourceDir, targetDir, fileTypes, types, ignoreDir, ignoreFile);
            }
         }
      }
      else {
         if(!ignoreFile.contains(file.getName())) {
            String ap = file.getAbsolutePath();
            String t = ap.substring(ap.lastIndexOf(".") + 1, ap.length());
            String tf = targetDir + ap.replace(sourceDir, "");

            if(types != null) {
               String tp = "";
               if(types instanceof Map) {
                  tp = ((Map<String, String>)types).get(t);
               }else {
                  tp = types.toString();
               }

               tf = tf.replace(t, "") + tp;
            }

            if(fileTypes == null || fileTypes.contains(t)) {
               tf = tf.replace("\\", "/");
               String dir = tf.substring(0, tf.lastIndexOf("/"));
               File dirFile = new File(dir);
               File targetFile = new File(tf);

               if(!targetFile.exists()) {
                  dirFile.mkdirs();
                  targetFile.createNewFile();
               }

               System.out.println("copy file " + ap + " to " + tf);
               copy(file, new File(tf));
            }
         }
      }
   }

   private static Log log = LogFactory.getLog(YHFileUtil.class);
}
