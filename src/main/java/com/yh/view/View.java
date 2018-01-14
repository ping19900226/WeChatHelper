package com.yh.view;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.io.File;
import java.util.*;

public abstract class View extends Application {

   public static final int TITLE_HEIGHT = 40;
   public static final int INIT_CONTENT_WIDTH = 800;
   public static final int INIT_CONTENT_HEIGHT = 600;
   public static final int SCROLL_BAR_WIDTH = 15;

   public final void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      primaryStage.setWidth(getPrefWidth());
      root = new AnchorPane();
      root.setPrefWidth(getPrefWidth());
      root.setPrefHeight(getPrefHeight());
      root.prefWidthProperty().bind(primaryStage.widthProperty());
      primaryStage.setScene(new Scene(root));
      views = new ArrayList<View>();

      //primaryStage.getScene().getStylesheets().add(Thread.currentThread().getContextClassLoader().getResource("style.css"));

      primaryStage.getIcons().add(new Image("http://pic.90sjimg.com/design/00/07/85/23/59316fe4b66ae.png"));

      start0(root);

//      primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
//         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//            getRoot().setPrefWidth(newValue.doubleValue());
//         }
//      });

      primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            getRoot().setPrefHeight(newValue.doubleValue() - TITLE_HEIGHT);
         }
      });

      primaryStage.show();
      afterOpen();

      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent event) {
            close();
         }
      });
   }

   public final void openView(View v) {
      if(v.unique() && views.contains(v)) {
         v.show();
         return;
      }

      try {
         openView0(v);
         views.add(v);
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   public final void close() {

      if(views.size() > 0) {
         Iterator<View> it = views.iterator();

         while(it.hasNext()) {
            View v = it.next();
            v.close();
            it.remove();
         }
      }
      else {
         primaryStage.close();
      }

      afterClose();
   }

   public int getPrefWidth() {
      return INIT_CONTENT_WIDTH;
   }

   public int getPrefHeight() {
      return INIT_CONTENT_HEIGHT;
   }

   public boolean unique() {
      return false;
   }

   public boolean modal() {
      return false;
   }

   private final Stage getPrimaryStage() {
      return primaryStage;
   }

   public final AnchorPane getRoot() {
      return root;
   }

   public void show() {
      primaryStage.show();
   }

   public void setTitle(String title) {
      primaryStage.setTitle(title);
   }

   public void afterOpen() {
      // do nothing
   }

   public void afterClose() {
      // do nothing
   }

   public File showChooseDirDialog() {
      return showChooseDirDialog(null);
   }

   public File showChooseDirDialog(String title) {
      DirectoryChooser dc = new DirectoryChooser();
      dc.setTitle(title == null ? "选择目录" : title);
      File dir = dc.showDialog(primaryStage);
      return dir;
   }

   public File showOpenFileDialog(String title) {
      FileChooser fc = new FileChooser();
      fc.setTitle(title == null ? "打开文件" : title);
      File dir = fc.showOpenDialog(primaryStage);
      return dir;
   }

   public File showSaveFileDialog(String title) {
      FileChooser fc = new FileChooser();
      fc.setTitle(title == null ? "保存文件" : title);
      File dir = fc.showSaveDialog(primaryStage);
      return dir;
   }

   public String getId() {
      return this.getClass().getSimpleName();
   }

   @Override
   public final int hashCode() {
      return getId().hashCode();
   }

   @Override
   public final boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(obj instanceof View) {
         View v = (View) obj;

         if(v.getId().equals(this.getId())) {
            return true;
         }
      }

      return false;
   }

   public abstract void start0(AnchorPane root) throws Exception;

   private void openView0(View v) throws Exception {
      Stage stage = new Stage();
      stage.initOwner(primaryStage.getScene().getWindow());
      v.start(stage);
   }

   private Stage primaryStage;
   private AnchorPane root;
   private List<View> views;
}
