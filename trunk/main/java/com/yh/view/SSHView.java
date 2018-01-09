package com.yh.view;

import com.yh.core.SSHConnection;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class SSHView extends Application{

   private ScrollPane root;
   private SSHConnection conn;
   private TextArea lastInput;

   public void start(Stage primaryStage) throws Exception {
      final VBox contentPane = new VBox();
      contentPane.setFillWidth(true);
      contentPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
      root = new ScrollPane(contentPane);
      root.setFitToHeight(true);
      root.setFitToWidth(true);
      conn = new SSHConnection();
      conn.connect("47.91.249.95", "root", "LOVE0226wang", 22);

      final TextArea input = new TextArea();
      input.prefWidthProperty().bind(contentPane.prefWidthProperty());
      input.prefHeightProperty().bind(contentPane.prefHeightProperty());
      contentPane.getChildren().add(input);

      final TextArea input2 = new TextArea();
      input2.prefWidthProperty().bind(contentPane.prefWidthProperty());
      input2.setPrefHeight(50);
      lastInput = input2;
      contentPane.getChildren().add(input2);

      primaryStage.setScene(new Scene(root));
      primaryStage.show();


      contentPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
         public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER) {
               String rs = conn.execute(lastInput.getText());
               input.setEditable(false);
               input.appendText(lastInput.getText());
               input.appendText(rs);

               input.setPrefHeight(input.getPrefColumnCount() * 30);
               lastInput.setPrefHeight(Region.USE_COMPUTED_SIZE);

               input2.clear();

            }
         }
      });



   }

   public static void main(String[] args) {
      launch();
   }
}
