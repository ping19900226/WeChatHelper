package com.yh.core;

import javafx.scene.control.Alert;
import jcifs.smb.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class YHFileUtil {

   public static void downloadShareFile(String dir, YHFile file, ShareConnection conn) {
      // 数组过大竟然会引起文件损坏，损坏原因是byte未填满，自动补0的问题
      byte[] bs = new byte[10 * 1024];
      SmbFileInputStream is = null;
      FileOutputStream fos = null;

      try {
         is = (SmbFileInputStream) conn.loadFile(file);
         fos = new FileOutputStream(dir + "/" + file.getName());
         int count = 0;
         int read = 0;
         int t = file.getSize();

         if(t - count <= 10 * 1024) {
            bs = new byte[t - count];
         }

         while((count = is.read(bs)) != -1) {
            fos.write(bs);
            read += count;

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(file.getModifyTime());
            String date = new SimpleDateFormat().format(c.getTime());
            System.out.println(read + " --> " + file.getSize() + " --> " + date);
         }

         fos.flush();
         new Alert(Alert.AlertType.INFORMATION,"下载完成").show();
      }
      catch(IOException e) {
         e.printStackTrace();
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
         // ingore
      }

      return f;
   }
}
