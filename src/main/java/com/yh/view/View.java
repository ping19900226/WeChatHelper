package com.yh.view;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import com.yh.core.Connection;
import com.yh.view.component.YHVBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import jcifs.smb.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class View extends Application{

   private FlowPane contentPane;
   private Connection conn;
   private Stage stage;
   private Stack<String> history = new StringStack();
   private String lastPath = null;
   private Label tipLabel;
   private AnchorPane menuBarPane;

   private static final int TITLE_HEIGHT = 40;
   private static final int STATUS_BAR_HEIGHT = 30;
   private static final int INIT_CONTENT_WIDTH = 800;
   private static final int INIT_CONTENT_HEIGHT = 600;
   private static final int SCROLL_BAR_WIDTH = 15;
   private static final int MENU_BAR_HEIGHT = 25;

   public void start(Stage primaryStage) throws Exception {
      contentPane = new FlowPane();
      contentPane.setStyle("-fx-background-color: #FFFFFF");
      stage = primaryStage;
      primaryStage.getIcons().add(new Image("https://timgsa.baidu" +
         ".com/timg?image&quality=80&size=b9999_10000&sec=1515401013092&di=363aa3d528ec63cc5" +
         "fe74567fc317445&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fdesign%2F00%2F07%2F85%2F" +
         "23%2F59316fe4b66ae.png"));

      primaryStage.setMinWidth(INIT_CONTENT_WIDTH);
      primaryStage.setMinHeight(INIT_CONTENT_HEIGHT);
      
      final AnchorPane rootPane = new AnchorPane();
      rootPane.setMinWidth(INIT_CONTENT_WIDTH);
      rootPane.setPrefHeight(INIT_CONTENT_HEIGHT);

      menuBarPane = new AnchorPane();
      menuBarPane.setPrefWidth(INIT_CONTENT_WIDTH);
      menuBarPane.setPrefHeight(MENU_BAR_HEIGHT);
      menuBarPane.setLayoutX(0);
      menuBarPane.setLayoutY(0);
      rootPane.getChildren().add(menuBarPane);
      addMenuBar();

      contentPane.setPrefWidth(INIT_CONTENT_WIDTH);
      contentPane.setPrefHeight(INIT_CONTENT_HEIGHT - STATUS_BAR_HEIGHT - MENU_BAR_HEIGHT);

      final ScrollPane scrollPane = new ScrollPane(contentPane);
      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);
      scrollPane.setLayoutY(MENU_BAR_HEIGHT);
      rootPane.getChildren().add(scrollPane);

      tipLabel = new Label("欢迎");
      tipLabel.setPrefHeight(STATUS_BAR_HEIGHT);
      tipLabel.setLayoutX(0);
      tipLabel.setLayoutY(INIT_CONTENT_HEIGHT - STATUS_BAR_HEIGHT);
      tipLabel.prefWidthProperty().bind(primaryStage.widthProperty());
      rootPane.getChildren().add(tipLabel);
      primaryStage.setScene(new Scene(rootPane));
      primaryStage.setTitle("");
      primaryStage.show();

      conn =  new Connection("192.168.1.134", "root", "forDEV123", "");

      list("", false);

      primaryStage.setTitle("192.168.1.134");
      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent event) {
            System.exit(0);
         }
      });

      primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            contentPane.setPrefWidth(newValue.doubleValue() - SCROLL_BAR_WIDTH);
            rootPane.setPrefWidth(newValue.doubleValue());
         }
      });

      primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            contentPane.setPrefHeight(newValue.doubleValue() - TITLE_HEIGHT - MENU_BAR_HEIGHT - STATUS_BAR_HEIGHT);
            rootPane.setPrefHeight(newValue.doubleValue() - TITLE_HEIGHT - MENU_BAR_HEIGHT - STATUS_BAR_HEIGHT);
            tipLabel.setLayoutY(newValue.doubleValue() - TITLE_HEIGHT - STATUS_BAR_HEIGHT);
         }
      });
   }

   private void addMenuBar() {
      MenuBar bar = new MenuBar();
      bar.prefWidthProperty().bind(stage.widthProperty());
      bar.setPrefHeight(MENU_BAR_HEIGHT);
      menuBarPane.getChildren().add(bar);

      Menu menu = new Menu("功能");
      MenuItem create = new MenuItem("新建资源");
      MenuItem store = new MenuItem("保存");
      MenuItem task = new MenuItem("定时任务");

      menu.getItems().addAll(create, store, task);
      bar.getMenus().add(menu);
   }

   private void list(String path, boolean back) {
      tipLabel.setText("正在打开");
      if(lastPath != null && !back) {
         history.add(lastPath);
      }

      lastPath = path;
      contentPane.getChildren().clear();


      int fc = 0;
      int dc = 0;

      try {
         List<SmbFile> fs = conn.listChild(path);

         if(fs != null) {
            for(SmbFile f : fs) {

               VBox b;

               final ContextMenu m = new ContextMenu();

               MenuItem open = new MenuItem();
               open.setUserData(f);
               m.getItems().add(open);

               if(f.isDirectory()) {
                  dc ++;
                  b = new YHVBox("http://pic.58pic.com/58pic/14/81/65/92W58PICS7s_1024.jpg", f
                     .getName());
                  open.setText("打开");
               }
               else {
                  fc ++;
                  b = new YHVBox("http://img.article.pchome" +
                     ".net/00/35/33/84/pic_lib/wm/Crystal_Generic.jpg", f.getName());
                  open.setText("下载");
               }

               b.setUserData(f);
               contentPane.getChildren().add(b);

               b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  public void handle(MouseEvent event) {
                     if(event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                        VBox box = (VBox) event.getSource();
                        SmbFile file = (SmbFile) box.getUserData();
                        handleClick(file);
                     }
                  }
               });

               //open.onActionProperty().bind(b.onMouseClickedProperty());

               b.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                  public void handle(ContextMenuEvent event) {
                     VBox box = (VBox) event.getSource();
                     m.show(box, event.getScreenX(), event.getScreenY());
                  }
               });

               open.setOnAction(new EventHandler<ActionEvent>() {
                  public void handle(ActionEvent event) {
                     MenuItem box = (MenuItem) event.getSource();
                     SmbFile file = (SmbFile) box.getUserData();
                     handleClick(file);
                  }
               });
            }
         }

         if(!path.equals("")) {
            addBack(path);
         }
         else {
            history.clear();
         }

      }
      catch(IOException e) {
         addBack(path);
         new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
      }

      tipLabel.setText("完毕\t总数：" + (dc + fc) + "\t文件：" + fc + "\t目录：" + dc);
   }

   private void handleClick(SmbFile file) {
      try {
         if(file.isDirectory()) {
            list(file.getURL().toString(), false);
         }
         else {
            download(file);
         }
      }
      catch(SmbException e) {
         // ignore
      }
   }

   private void addBack(String path) {
      VBox b = new YHVBox("http://ico.ooopic.com/iconset01/pretty-office-icons-v5/gif/93212.gif",
         "返回");
      b.setUserData(path);

      b.setOnMouseClicked(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            if(event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
               contentPane.getChildren().clear();
               list(history.pop(), true);
            }
         }
      });

      contentPane.getChildren().add(b);
   }

   private void download(SmbFile file) {
      FileChooser fileChooser = new FileChooser();
      DirectoryChooser dc = new DirectoryChooser();
      dc.setTitle("选择保存的目录");
      File dir = dc.showDialog(stage);

      // 数组过大竟然会引起文件损坏，损坏原因是byte未填满，自动补0的问题
      byte[] bs = new byte[10 * 1024];
      SmbFileInputStream is = null;
      FileOutputStream fos = null;

      try {
         is = (SmbFileInputStream) conn.loadFile(file);
         fos = new FileOutputStream(dir + "/" + file.getName());
         int count = 0;
         int read = 0;
         int t = file.getContentLength();

         if(t - count <= 10 * 1024) {
            bs = new byte[t - count];
         }

         while((count = is.read(bs)) != -1) {
            fos.write(bs);
            read += count;

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(file.getLastModified());
            String date = new SimpleDateFormat().format(c.getTime());
            System.out.println(read + " --> " + file.getContentLength() + " --> " + date);
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

   public static void main(String[] args) {
      launch();
   }
}
