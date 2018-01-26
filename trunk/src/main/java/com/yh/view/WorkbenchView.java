package com.yh.view;

import com.yh.util.Resource;
import com.yh.util.Style;
import com.yh.view.component.ImageButton;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class WorkbenchView extends View{

   private GridPane quickBtnPane;

   @Override
   public void start0(AnchorPane root) throws Exception {
      Style.setStyle(root, "-fx-background-color:#FFFFFF;");
      setTitle("工作台");

      quickBtnPane = new GridPane();
      quickBtnPane.prefWidthProperty().bind(root.widthProperty());
      quickBtnPane.setPrefHeight(100);
      quickBtnPane.setPadding(new Insets(10));
      root.getChildren().add(quickBtnPane);

      addQuickBtn();
   }

   @Override
   public int getPrefWidth() {
      return 380;
   }

   @Override
   public int getPrefHeight() {
      return 500;
   }

   @Override
   public void afterClose() {
      System.exit(0);
   }

   @Override
   public boolean isMaximized() {
      return false;
   }

   private void addQuickBtn() {
      ImageButton shareBtn = new ImageButton("文件共享", Resource.getImagePath("share.png"));

      shareBtn.setOnAction(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            openView(new ShareFileView("192.168.1.134", "root", "forDEV123"), true);
         }
      });

      quickBtnPane.add(shareBtn, 0, 0);

      ImageButton bugBtn = new ImageButton("BUG系统", Resource.getImagePath("bug.png"));

      bugBtn.setOnAction(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            openView(new BugListView());
         }
      });

      quickBtnPane.add(bugBtn, 1, 0);

   }
}
