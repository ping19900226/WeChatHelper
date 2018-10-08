package com.yh.common.dialog;

import com.yh.common.request.Callback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class Confirm extends Alert{
   public static final byte CANCEL= 2;
   private String cancelBtntitle;

   public Confirm(String content, Callback<Byte> callback) {
      super(content, callback);
   }

   public Confirm(String title, String content, Callback<Byte> callback) {
      super(title, content, callback);
   }

   @Override
   protected void buildButton(VBox container) {
      Button cancelBtn = new Button(getCancelBtntitle() == null ? "取消" : getCancelBtntitle());

      FlowPane cancelPane = new FlowPane();
      cancelPane.setAlignment(Pos.CENTER_RIGHT);
      cancelPane.getChildren().add(cancelBtn);

      container.getChildren().add(cancelPane);

      cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            callback(CANCEL);
         }
      });

      Button okBtn = new Button(getOkBtnTitle() == null ? "确定" : getOkBtnTitle());

      FlowPane btnPane = new FlowPane();
      btnPane.setAlignment(Pos.CENTER_RIGHT);
      btnPane.getChildren().add(okBtn);

      container.getChildren().add(btnPane);

      okBtn.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent event) {
            callback(OK);
         }
      });
   }

   String getCancelBtntitle() {
      return cancelBtntitle;
   }

   public void setCancelBtntitle(String cancelBtntitle) {
      this.cancelBtntitle = cancelBtntitle;
   }

   private void callback(byte code) {
      this.close();
      getCallback().call(code);
   }
}
