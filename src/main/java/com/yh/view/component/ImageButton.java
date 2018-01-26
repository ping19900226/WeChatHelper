package com.yh.view.component;

import com.yh.util.StringUtil;
import com.yh.util.Style;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ImageButton extends VBox {

   public ImageButton(String icon, int width, int height) {
      build(null, icon, width, height);
   }

   public ImageButton(String text, String icon, int width, int height) {
      build(text, icon, width, height);
   }

   public ImageButton(String text, String icon) {
      build(text, icon, 60, 60);
   }

   private void build(String text, String icon, int width, int height) {
      ImageView image = new ImageView(new Image(icon));
      image.setFitWidth(width - 10);
      image.setFitHeight(height - 10);
      //VBox.setMargin(image, new Insets(10));
      this.getChildren().add(image);

      if(text != null) {
         Label label = new Label();
         label.setWrapText(true);
         label.setPrefWidth(width - 10);
         label.setPrefHeight(30);
         label.setMinHeight(30);
         label.setText(text);

         this.getChildren().add(label);
      }

      this.setCursor(Cursor.HAND);
      this.setPrefWidth(width);
      this.setPrefHeight(text == null ? height : height + 30);
      this.setAlignment(Pos.CENTER);
      this.setCursor(Cursor.HAND);
      this.setPadding(new Insets(10));

      Style style = Style.parse(this.getStyle());
      style.setStyle("-fx-border-width: 1px");
      style.setStyle("-fx-border-color: #FFFFFF");
      this.setStyle(style.toString());

      addEvent();
   }

   private void addEvent() {
      this.setOnMouseEntered(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            VBox b = (VBox) event.getSource();
            Style style = Style.parse(b.getStyle());
            style.setStyle("-fx-background-color: rgba(235,235,235,.6);");
            style.setStyle("-fx-border-width: 1px;");
            style.setStyle("-fx-border-color: #0092D2");

            b.setStyle(style.toString());
         }
      });

      this.setOnMouseExited(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            VBox b = (VBox) event.getSource();

            Style style = Style.parse(b.getStyle());
            style.setStyle("-fx-background-color: rgb(255,255,255);");
            style.setStyle("-fx-border-width: 1px");
            style.setStyle("-fx-border-color: #FFFFFF");

            b.setStyle(style.toString());
         }
      });
   }

   public void setOnAction(EventHandler<MouseEvent> e) {
      this.setOnMouseClicked(e);
   }

   public Style getStyles() {
      return Style.parse(this.getStyle());
   }

   public void setStyles(Style style) {
      this.setStyle(style.toString());
   }
}
