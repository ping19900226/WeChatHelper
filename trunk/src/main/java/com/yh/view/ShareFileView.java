package com.yh.view;

import com.yh.core.*;
import com.yh.view.component.YHVBox;
import com.yh.view.data.YHFileStack;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import java.io.*;
import java.util.*;

public class ShareFileView extends View{

   public void start0(AnchorPane root) throws Exception {
      contentPane = new FlowPane();
      contentPane.setStyle("-fx-background-color: #FFFFFF");

      menuBarPane = new AnchorPane();
      menuBarPane.setPrefWidth(INIT_CONTENT_WIDTH);
      menuBarPane.setPrefHeight(MENU_BAR_HEIGHT);
      menuBarPane.setLayoutX(0);
      menuBarPane.setLayoutY(0);
      getRoot().getChildren().add(menuBarPane);
      addMenuBar();

      contentPane.setPrefWidth(INIT_CONTENT_WIDTH);
      contentPane.setPrefHeight(INIT_CONTENT_HEIGHT - STATUS_BAR_HEIGHT - MENU_BAR_HEIGHT);

      final ScrollPane scrollPane = new ScrollPane(contentPane);
      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);
      scrollPane.setLayoutY(MENU_BAR_HEIGHT);
      getRoot().getChildren().add(scrollPane);

      tipLabel = new Label("欢迎");
      tipLabel.setPrefHeight(STATUS_BAR_HEIGHT);
      tipLabel.setLayoutX(0);
      tipLabel.setLayoutY(INIT_CONTENT_HEIGHT - STATUS_BAR_HEIGHT);
      tipLabel.prefWidthProperty().bind(getRoot().widthProperty());
      getRoot().getChildren().add(tipLabel);
      //primaryStage.setScene(new Scene(rootPane));

      conn =  new ShareConnection("192.168.1.134", "root", "forDEV123");

      list(null, false);

      setTitle("192.168.1.134");


      root.widthProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            contentPane.setPrefWidth(newValue.doubleValue());
         }
      });

      root.heightProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            contentPane.setPrefHeight(newValue.doubleValue() - MENU_BAR_HEIGHT - STATUS_BAR_HEIGHT);
            tipLabel.setLayoutY(newValue.doubleValue() - STATUS_BAR_HEIGHT);
         }
      });
   }

   @Override
   public boolean unique() {
      return true;
   }

   private void addMenuBar() {
      MenuBar bar = new MenuBar();
      bar.prefWidthProperty().bind(getRoot().widthProperty());
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
      File dir = showChooseDirDialog("选择保存的目录");

      boolean b = YHFileUtil.downloadShareFile(dir.getAbsolutePath(), file, conn);

      if(b) {
         new Alert(Alert.AlertType.INFORMATION,"下载完成").show();
      }
      else {
         new Alert(Alert.AlertType.ERROR,"下载失败").show();
      }
   }

   private static final int STATUS_BAR_HEIGHT = 30;
   private static final int MENU_BAR_HEIGHT = 25;

   private FlowPane contentPane;
   private ShareConnection conn;
   private Stack<YHFile> history = new YHFileStack();
   private YHFile lastPath = null;
   private Label tipLabel;
   private AnchorPane menuBarPane;
}
