package com.yh.view.component;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class YHVBox extends VBox {

   public YHVBox(String icon, String title) {
      super();
      this.setOnMouseEntered(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            VBox b = (VBox) event.getSource();
            b.setStyle(b.getStyle().contains("-fx-background-color:") ? b.getStyle().replace(
               "-fx-background-color: rgb(255,255,255);",
               "-fx-background-color: rgba(235,235,235,.6);") :
               "-fx-background-color: rgba(235,235,235,.6);");
         }
      });

      this.setOnMouseExited(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            VBox b = (VBox) event.getSource();
            b.setStyle(b.getStyle().replace("-fx-background-color: rgba(235,235,235,.6);",
               "-fx-background-color: rgb(255,255,255);"));
         }
      });

      this.setCursor(Cursor.HAND);
      this.setPrefWidth(100);
      this.setPrefHeight(140);
      this.setAlignment(Pos.CENTER);

      ImageView img = new ImageView();
      img.setFitWidth(80);
      img.setFitHeight(80);
      VBox.setMargin(img, new Insets(10));
      Image image = new Image(icon);
      img.setImage(image);
      this.getChildren().add(img);
      FlowPane.setMargin(this, new Insets(10));
      this.getChildren().add(new Label(title.replace("\\", "").replace("/", "")));
   }
}
