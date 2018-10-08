package com.yh.view;

import com.jcraft.jsch.ChannelShell;
import com.yh.common.view.View;
import com.yh.core.SSHConnection;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SSHView extends View {

   private ScrollPane content;
   private SSHConnection conn;
   private TextArea input;
   private TextArea result;
   private ChannelShell shell;
   private boolean read = true;
   private InputStream is;
   private OutputStream os;

   public void start0(AnchorPane root) throws Exception {
      final VBox contentPane = new VBox();
      contentPane.setFillWidth(true);
      contentPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
      content = new ScrollPane(contentPane);
      content.setFitToHeight(true);
      content.setFitToWidth(true);
      root.getChildren().add(content);
      conn = new SSHConnection();
      conn.connect("47.91.249.95", "root", "LOVE0226wang", 22);
      os = new BufferedOutputStream(new ByteArrayOutputStream());
      final InputStream is0 = new PipedInputStream();
      os = new PipedOutputStream((PipedInputStream) is0);

      final OutputStream os0 = new PipedOutputStream();
      is = new PipedInputStream((PipedOutputStream) os0);


      shell = conn.execute(is0, os0);

      result = new TextArea();
      result.prefWidthProperty().bind(contentPane.prefWidthProperty());
      result.setPrefHeight(0);
      contentPane.getChildren().add(result);

      input = new TextArea();
      input.prefWidthProperty().bind(contentPane.prefWidthProperty());
      input.setPrefHeight(50);
      contentPane.getChildren().add(input);

      contentPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
         public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER) {
               //List<String> rs = conn.execute(lastInput.getText());
               String text = input.getText();
               String cmd = "";
               if(text.contains("#") && !text.endsWith("#")) {
                  cmd = text.split("#")[1].trim();
               }

               result.setPrefHeight(result.getPrefHeight() + 20);
               result.appendText(text);
               result.appendText("\n");
               input.clear();

               try {
                  os.write(cmd.getBytes());
                  os.flush();
               } catch (IOException e) {
                  e.printStackTrace();
               }

               read = true;
               read();
            }
         }
      });

      read();

   }

   public void read() {
      while (read) {
         try {
            byte[] bs = new byte[1024 * 10];
            int length = 0;
            int c = is.available();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String buf = null;
            List<String> rs = new ArrayList<String>();
            boolean eof = false;

            byte[] lastbs = null;
            while (!eof && (is.read(bs)) > 0) {
               if(bs[0] == 0) {
                  eof = true;
                  break;
               }

               int lastIndex = 0;

               for(int i = 0; i < bs.length; i++) {
                  if(bs[i] == 0) {
                     byte[] bs0 = new byte[i - lastIndex];
                     System.arraycopy(bs, lastIndex, bs0, 0, i - lastIndex);
                     lastbs = bs0;
                     eof = true;
                     break;
                  }

                  if(bs[i] == 13 && bs[i + 1] == 10) {
                     byte[] bs0 = new byte[i + 2 - lastIndex];
                     System.arraycopy(bs, lastIndex, bs0, 0, i + 2 - lastIndex);
                     rs.add(new String(bs0).replace("\r", "").replace("\n", ""));
                     i ++;
                     lastIndex = i;
                  }
               }


            }

            rs.add(new String(lastbs).replace("\r", "").replace("\n", ""));

//            while ((buf = reader.readLine()) != null && !buf.trim().isEmpty()) {
//               rs.add(buf);
//            }

            String lt = rs.get(rs.size() - 1);
            int size = lt.contains("#") ? rs.size() - 1 : rs.size();

            for(int i = 0; i < size; i++) {
               result.appendText(rs.get(i));
               result.setPrefHeight(result.getPrefHeight() + 20);
            }

            input.setText(lt);


            //is.reset();

            System.out.println("read all");
         }
         catch (IOException e) {
            e.printStackTrace();
         }
         read = false;
      }
   }

   @Override
   public void afterClose() {
      shell.disconnect();
   }
}
