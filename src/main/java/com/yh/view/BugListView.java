package com.yh.view;

import com.yh.request.BugzillaRequestHandler;
import com.yh.request.RequestHandler;
import com.yh.request.entity.Bug;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.util.List;;

public class BugListView extends View{
   public void start0(AnchorPane root) throws Exception {
      handler = (BugzillaRequestHandler) RequestHandler.getRequestHandler(1);
      RequestHandler.AuthenticationInfo info = handler.getDefaultAuthenticationInfo();

      if(info == null || !info.isLogin()) {
         handler.login("yihuanwang@yonghongtech.com", "yihuan");
      }

      view = new TableView();

      view.prefWidthProperty().bind(root.widthProperty());
      view.selectionModelProperty().addListener(new ChangeListener() {
         public void changed(ObservableValue observable, Object oldValue, Object newValue) {

         }
      });
      view.setOnMouseClicked(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            if(event.getButton() == MouseButton.PRIMARY &&
               event.getClickCount() == 2) {
               TableView v = (TableView) event.getSource();
               int index = v.getSelectionModel().getSelectedIndex();
               Bug bug = (Bug) v.getItems().get(index);

               String url = "http://192.168.1.120//show_bug.cgi?id=" + bug.getId();
               try {
                  openView(new ExplorerView(url));
               }
               catch(Exception e) {
                  e.printStackTrace();
               }
            }
         }
      });

      root.getChildren().add(view);
      buildTableHeader();
      buildTableBody();

      root.heightProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            view.setPrefHeight(newValue.doubleValue());
         }
      });

   }

   private void buildTableHeader() {
      TableColumn id = new TableColumn();
      id.setText("ID");

      TableColumn type = new TableColumn();
      type.setText("类型");

      TableColumn priority = new TableColumn();
      priority.setText("优先级");

      TableColumn reporter = new TableColumn();
      reporter.setText("报告人");

      TableColumn status = new TableColumn();
      status.setText("状态");

      TableColumn summary = new TableColumn();
      summary.setText("简介");

      view.getColumns().addAll(id, type, priority, reporter, status, summary);
   }

   private void buildTableBody() {
      List<Bug> bugs = handler.getBugList();

      ObservableList<Bug> data = FXCollections.observableArrayList(bugs);

      ObservableList<TableColumn> observableList = view.getColumns();
      observableList.get(0).setCellValueFactory(new PropertyValueFactory("id"));
      observableList.get(1).setCellValueFactory(new PropertyValueFactory("type"));
      observableList.get(2).setCellValueFactory(new PropertyValueFactory("priority"));
      observableList.get(3).setCellValueFactory(new PropertyValueFactory("reporter"));
      observableList.get(4).setCellValueFactory(new PropertyValueFactory("status"));
      observableList.get(5).setCellValueFactory(new PropertyValueFactory("summary"));
      view.setItems(data);

   }

   private TableView view;
   private BugzillaRequestHandler handler;
}
