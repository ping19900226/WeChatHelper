package com.yh.common.dialog;

import com.yh.common.request.Callback;
import com.yh.util.Style;
import com.yh.common.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class Alert extends View {

   public static final byte OK = 1;
   private String title;
   private String content;
   private String okBtnTitle;
   private Callback<Byte> callback;

   private Alert(){}

   public Alert(String content) {
      this.content = content;
   }

   public Alert(String content, Callback<Byte> callback) {
      this.content = content;
      this.callback = callback;
   }

   public Alert(String title, String content) {
      this.title = title;
      this.content = content;
   }

   public Alert(String title, String content, Callback<Byte> callback) {
      this.title = title;
      this.content = content;
      this.callback = callback;
   }


   public void start0(AnchorPane root) throws Exception {
      Style.setStyle(root, "-fx-background-color:#FFFFFF;-fx-padding:5");
      root.setPadding(new Insets(5));
      VBox container = new VBox();
      buildContent(container);
      buildButton(container);
      root.getChildren().add(container);
   }

   public String getTitle() {
      return title == null ? "提示" : title;
   }

   public void setOkBtnTitle(String okBtnTitle) {
      this.okBtnTitle = okBtnTitle;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public void setCallback(Callback<Byte> callback) {
      this.callback = callback;
   }

   @Override
   public int getPrefWidth() {
      return 200;
   }

   @Override
   public int getPrefHeight() {
      return 120;
   }

   @Override
   public boolean isMaximized() {
      return false;
   }

   protected void buildContent(VBox container) {

      Label content = new Label();
      content.setMinHeight(40);
      content.setMaxHeight(100);
      content.setText(getContent());
      container.setPrefWidth(getPrefWidth());
      container.getChildren().add(content);
   }

   protected void buildButton(VBox container) {
      Button okBtn = new Button(getOkBtnTitle() == null ? "确定" : getOkBtnTitle());
      Style.setStyle(okBtn, "-fx-background-color:#FFFFFF;" +
         "-fx-border-width:1;-fx-border-color:#CCCCCC;");
      okBtn.setPadding(new Insets(5, 10, 5, 10));

      FlowPane btnPane = new FlowPane();
      btnPane.setAlignment(Pos.CENTER);
      btnPane.getChildren().add(okBtn);
      btnPane.setPrefWidth(getPrefWidth());

      container.getChildren().add(btnPane);

      final View that = this;

      okBtn.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            that.close();

            if(getCallback() != null)
               getCallback().call(OK);
         }
      });
   }

   String getContent() {
      return content;
   }

   String getOkBtnTitle() {
      return okBtnTitle;
   }

   Callback<Byte> getCallback() {
      return callback;
   }
}