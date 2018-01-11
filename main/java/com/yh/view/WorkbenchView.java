package com.yh.view;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class WorkbenchView extends View{

   @Override
   public void start0(AnchorPane root) throws Exception {
      quickBtnPane = new GridPane();
      quickBtnPane.add(new Button("share"), 0, 0);
      quickBtnPane.add(new Button("bug"), 1, 0);
      quickBtnPane.add(new Button("sql"), 0, 1);
      root.getChildren().add(quickBtnPane);
   }

   private GridPane quickBtnPane;
}
