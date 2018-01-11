package com.yh.view.component;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QuickBtn extends Button {

   public QuickBtn(String text) {
      super(text);
      ImageView bugImage = new ImageView(new Image("http://pic.58pic.com/58pic/14/81/65/92W58PICS7s_1024.jpg"));
      this.setGraphic(bugImage);
   }


}
