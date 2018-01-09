package com.yh.view;

import com.yh.core.*;
import com.yh.view.component.YHVBox;
import com.yh.view.data.YHFileStack;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class View extends Application{

   private FlowPane contentPane;
   private ShareConnection conn;
   private Stage stage;
   private Stack<YHFile> history = new YHFileStack();
   private YHFile lastPath = null;
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
      primaryStage.getIcons().add(new Image("http://pic.90sjimg.com/design/00/07/85/23/59316fe4b66ae.png"));

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

      conn =  new ShareConnection("192.168.1.134", "root", "forDEV123");

      list(null, false);

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

   private void list(YHFile file, boolean back) {
      file = file == null ? YHFile.emptyFile() : file;

      if(lastPath != null && !back) {
         history.add(lastPath);
      }

      lastPath = file;
      contentPane.getChildren().clear();

      int fc = 0;
      int dc = 0;

      try {
         List<YHFile> fs = conn.listChild(file);

         if(fs != null) {
            for(YHFile f : fs) {

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
                        YHFile file = (YHFile) box.getUserData();
                        handleClick(file);
                     }
                  }
               });

               b.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                  public void handle(ContextMenuEvent event) {
                     VBox box = (VBox) event.getSource();
                     m.show(box, event.getScreenX(), event.getScreenY());
                  }
               });

               open.setOnAction(new EventHandler<ActionEvent>() {
                  public void handle(ActionEvent event) {
                     MenuItem box = (MenuItem) event.getSource();
                     YHFile file = (YHFile) box.getUserData();
                     handleClick(file);
                  }
               });
            }
         }

         if(file != null && !file.isEmpty()) {
            addBack(file);
         }
         else {
            history.clear();
         }

      }
      catch(IOException e) {
         addBack(file);
         new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
      }

      tipLabel.setText("完毕\t总数：" + (dc + fc) + "\t文件：" + fc + "\t目录：" + dc);
   }

   private void handleClick(YHFile file) {
      if(file.isDirectory()) {
         list(file, false);
      }
      else {
         download(file);
      }
   }

   private void addBack(YHFile file) {
      VBox b = new YHVBox("http://ico.ooopic.com/iconset01/pretty-office-icons-v5/gif/93212.gif",
         "返回");
      b.setUserData(file);

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

   private void download(YHFile file) {
      FileChooser fileChooser = new FileChooser();
      DirectoryChooser dc = new DirectoryChooser();
      dc.setTitle("选择保存的目录");
      File dir = dc.showDialog(stage);

      YHFileUtil.downloadShareFile(dir.getAbsolutePath(), file, conn);

   }

   public void openView(View v) {
      try {
         v.start(stage);
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      launch();
   }
}
