package com.yh.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
   private SSHConnection conn;

   public void start() throws IOException {
      conn = new SSHConnection();
      conn.connect("47.91.249.95", "root", "LOVE0226wang", 22);

      //为了简单起见，所有的异常信息都往外抛
      int port = 10226;
      //定义一个ServerSocket监听在端口8899上
      ServerSocket server = new ServerSocket(port);
      //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
      while(true) {
         Socket socket = server.accept();
         //跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
         Reader reader = new InputStreamReader(socket.getInputStream());
         char chars[] = new char[64];
         int len;
         StringBuilder sb = new StringBuilder();
         while ((len=reader.read(chars)) != -1) {
            sb.append(new String(chars, 0, len));
         }
         System.out.println("from client: " + sb);



         OutputStream os = socket.getOutputStream();
         os.write(("Access-Control-Allow-Origin:*\n"
            + "Cache-Control:max-age=691200\n"
            + "Connection:keep-alive\n"
            + "Content-Length:0\n"
            + "Content-Type:application/octet-stream\n"
            + "Date:Tue, 09 Jan 2018 06:06:41 GMT\n"
            + "Etag:\"af7ae505a9eed503f8b8e6982036873e\"\n"
            + "Expires:Mon, 15 Jan 2018 04:20:37 GMT\n"
            + "Server:marco/1.12\n"
            + "Via:T.132.H, M.cun-ha-ayn-133\n"
            + "X-Request-Id:8bd82f32f10400bf686c69b8dd871502").getBytes());
         os.flush();
         os.close();

         reader.close();

      }
   }
}
