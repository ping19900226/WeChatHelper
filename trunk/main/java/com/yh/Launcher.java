package com.yh;

import com.yh.view.WorkbenchView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
   @Override
   public void start(Stage primaryStage) throws Exception {
      new WorkbenchView().start(new Stage());
   }

   public static void main(String[] args) {
      launch(args);
   }
}
