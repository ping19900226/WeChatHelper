package com.yh.view.component;

import javafx.event.EventHandler;
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
   }

   public void setOnAction(EventHandler<MouseEvent> e) {
      this.setOnMouseClicked(e);
   }


}
