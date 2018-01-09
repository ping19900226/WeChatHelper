package com.yh.core;

import com.jcraft.jsch.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Scanner;

public class SSHConnection {

   private Session session = null;

   public void connect(String host, String username, String password, int port) {
      JSch jsch = new JSch();
      try {
         session = jsch.getSession(username, host, port);
         session.setPassword(password);

         Properties config = new Properties();
         config.put("StrictHostKeyChecking", "no");
         session.setConfig(config); // 为Session对象设置properties
         int timeout = 60000000;
         session.setTimeout(timeout); // 设置timeout时间
         session.connect(); // 通过Session建立链接
         session.setPortForwardingL(10226, host,
            80);

      }
      catch(JSchException e) {
         e.printStackTrace();
      }
   }

   public String execute(String cmd) {
      ChannelExec channelExec = null;
      BufferedReader reader = null;

      try {
         channelExec = (ChannelExec) session.openChannel("exec");
         channelExec.setCommand(cmd);
         channelExec.setInputStream(null);
         channelExec.setErrStream(System.err);
         channelExec.connect();

         InputStream in = channelExec.getInputStream();
         reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
         String buf = null;
         StringBuilder sb = new StringBuilder();

         while ((buf = reader.readLine()) != null) {
            sb.append(buf).append("\n");
         }

         return sb.toString();

      }
      catch(IOException e) {
         e.printStackTrace();
         return null;
      }
      catch(JSchException e) {
         e.printStackTrace();
         return null;
      }
      finally {
         if(reader != null) {
            try {
               reader.close();
            }
            catch(IOException e) {
               e.printStackTrace();
            }
         }

         if(channelExec != null) {
            channelExec.disconnect();
         }

      }
   }

   public void close() {

   }

   public static void main(String [] arg) {
      new SSHConnection().connect("47.91.249.95", "root", "LOVE0226wang", 22);
   }
}
