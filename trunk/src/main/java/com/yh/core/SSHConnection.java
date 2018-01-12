package com.yh.core;

import com.jcraft.jsch.*;
import com.jcraft.jsch.agentproxy.*;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class SSHConnection {

   private Session session = null;

   public void connect(String host, String username, String password, int port) {
      JSch jsch = new JSch();
      try {

         Connector con = null;

         try {
            ConnectorFactory cf = ConnectorFactory.getDefault();
            con = cf.createConnector();
         }
         catch(AgentProxyException e){
            System.out.println(e);
         }

         if(con != null ){
            IdentityRepository irepo = new RemoteIdentityRepository(con);
            jsch.setIdentityRepository(irepo);
         }

         session = jsch.getSession(username, host, port);
         session.setPassword(password);

         Properties config = new Properties();
         config.put("StrictHostKeyChecking", "no");
         session.setConfig(config); // 为Session对象设置properties
         int timeout = 60000000;
         session.setTimeout(timeout); // 设置timeout时间

         //ProxyHTTP proxyhttp = new  ProxyHTTP (host, 80);

         //session.setProxy(proxyhttp);
         //ProxySOCKS4 ps = new ProxySOCKS4("127.0.0.1", 10226);
         //session.setProxy(ps);
         session.setOutputStream(System.out);
         session.connect(); // 通过Session建立链接


//         session.setPortForwardingR(80, "127.0.0.1",
//            10226);
         //int assinged_port= session.setPortForwardingL(10226, host, 10226);
        // System.out.println("localhost:"+assinged_port+" -> "+host+":" + 10226);
//         Channel channel=session.openChannel("shell");
//
//         ((ChannelShell)channel).setAgentForwarding(true);
//
//         channel.setInputStream(System.in);
//         channel.setOutputStream(System.out);
//
//         channel.connect();
      }
      catch(JSchException e) {
         e.printStackTrace();
      }
   }

   public List<String> execute(String cmd) {
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
         List<String> rs = new ArrayList<String>();

         while ((buf = reader.readLine()) != null) {
            rs.add(buf);
         }

         return rs;

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

   public ChannelShell execute(InputStream is, OutputStream os) {
      try {
         ChannelShell shell = (ChannelShell) session.openChannel("shell");

         //os = channelExec.getOutputStream();
         //channelExec.setOutputStream(os);
//         channelExec.setInputStream(is);

         shell.setOutputStream(os);
         shell.setInputStream(is);
         shell.connect();
         //os = shell.getOutputStream();
         //is = shell.getInputStream();
         return shell;
      }
      catch(JSchException e) {
         e.printStackTrace();
      }

      return null;
   }

   public void close() {

   }
}
