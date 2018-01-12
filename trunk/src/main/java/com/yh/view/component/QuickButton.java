package com.yh.view.component;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class QuickButton extends VBox {

   public QuickButton(String text, String icon) {
      ImageView image = new ImageView(new Image(icon));
      image.setFitWidth(50);
      image.setFitHeight(50);
      this.getChildren().add(image);

      Label label = new Label();
      label.setWrapText(true);
      label.setText(text);
      this.getChildren().add(label);
      this.setCursor(Cursor.HAND);
      this.setPadding(new Insets(10));
   }

   private void addEvent() {
      this.setOnMouseEntered(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            VBox b = (VBox) event.getSource();
            b.setStyle(b.getStyle().contains("-fx-background-color:") ? b.getStyle().replace(
               "-fx-background-color: rgb(255,255,255);",
               "-fx-background-color: rgba(235,235,235,.6);") :
               "-fx-background-color: rgba(235,235,235,.6);");
         }
      });

      this.setOnMouseExited(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            VBox b = (VBox) event.getSource();
            b.setStyle(b.getStyle().replace("-fx-background-color: rgba(235,235,235,.6);",
               "-fx-background-color: rgb(255,255,255);"));
         }
      });
   }

   public void setOnAction(EventHandler<MouseEvent> e) {
      this.setOnMouseClicked(e);
   }
}
