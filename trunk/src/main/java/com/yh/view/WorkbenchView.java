package com.yh.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
      return 200;
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
      ImageView shareImage = new ImageView(new Image("http://pic.58pic.com/58pic/14/81/65/92W58PICS7s_1024.jpg"));
      shareImage.setFitWidth(50);
      shareImage.setFitHeight(50);
      Button shareBtn = new Button("share");
      shareBtn.setGraphic(shareImage);

      shareBtn.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            openView(new ShareFileView());
         }
      });

      quickBtnPane.add(shareBtn, 0, 0);

      ImageView bugImage = new ImageView(new Image("http://pic.58pic.com/58pic/14/81/65/92W58PICS7s_1024.jpg"));
      bugImage.setFitWidth(50);
      bugImage.setFitHeight(50);
      Button bugBtn = new Button("bug");
      bugBtn.setGraphic(bugImage);

      bugBtn.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            openView(new BugListView());
         }
      });

      quickBtnPane.add(bugBtn, 1, 0);
      quickBtnPane.add(new Button("sql"), 0, 1);
   }

   private GridPane quickBtnPane;
}
