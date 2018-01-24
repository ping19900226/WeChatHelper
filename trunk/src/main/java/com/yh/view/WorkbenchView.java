package com.yh.view;

import com.yh.view.component.QuickButton;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class WorkbenchView extends View{

   @Override
   public void start0(AnchorPane root) throws Exception {
      quickBtnPane = new GridPane();
      quickBtnPane.prefWidthProperty().bind(root.widthProperty());
      quickBtnPane.setPrefHeight(100);
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

   private void addQuickBtn() {
      QuickButton shareBtn = new QuickButton("share", "http://pic.58pic" +
         ".com/58pic/14/81/65/92W58PICS7s_1024.jpg");

      shareBtn.setOnAction(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            openView(new ShareFileView("192.168.1.134", "root", "forDEV123"));
         }
      });

      quickBtnPane.add(shareBtn, 0, 0);

      QuickButton bugBtn = new QuickButton("bug", "http://pic.58pic" +
         ".com/58pic/14/81/65/92W58PICS7s_1024.jpg");

      bugBtn.setOnAction(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            openView(new BugListView());
         }
      });

      quickBtnPane.add(bugBtn, 1, 0);
      quickBtnPane.add(new Button("sql"), 0, 1);
   }

   private GridPane quickBtnPane;
}
