package com.yh.view;

import com.yh.request.*;
import com.yh.request.entity.DataGrid;
import com.yh.util.Resource;
import com.yh.view.factory.ArrayValueFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.util.List;
import java.util.Properties;;

public class BugListView extends View{
   private TableView view;
   private BugzillaRequestHandler handler;
   private View self;
   private int crIdx;
   private Properties prop;

   public void start0(AnchorPane root) throws Exception {
      self = this;
      handler = (BugzillaRequestHandler) RequestHandler.getRequestHandler(1);
      RequestHandler.AuthenticationInfo info = handler.getDefaultAuthenticationInfo();
      prop = Resource.loadBugListProperties();

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
            }
         }
      });

      root.getChildren().add(view);
      DataGrid<List<String>> bugs = handler.getBugList();
      buildTableHeader(bugs.getHeaders());
      buildTableBody(bugs);

      view.setRowFactory(new Callback<TableView, TableRow>() {
         @Override
         public TableRow call(TableView param) {
            TableRow row = new TableRow();
            row.setMinHeight(40);
            row.setAlignment(Pos.CENTER);
            int size = param.getItems().size();

            if(crIdx >= 0 && crIdx < size) {
               List<String> ps = (List<String>) param.getItems().get(crIdx);
               row.setUserData(ps.get(0));

               row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                     if(event.getButton() == MouseButton.PRIMARY &&
                        event.getClickCount() == 2) {
                        TableRow v = (TableRow) event.getSource();
                        String url = "http://192.168.1.120//show_bug.cgi?id=" + v.getUserData().toString();
                        openView(new ExplorerView(url));
                     }
                  }
               });
            }

            crIdx ++;
            return row;
         }
      });

      root.heightProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            view.setPrefHeight(newValue.doubleValue());
         }
      });
   }

   private void buildTableHeader(List<String> headers) {
      for(String header : headers) {
         TableColumn id = new TableColumn();
         id.setText(header);

         ContextMenu menu = new ContextMenu();
         MenuItem item = new MenuItem("隐藏");
         menu.getItems().add(item);
         item.setUserData(id);
         id.setContextMenu(menu);
         id.setSortable(false);

         item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               MenuItem item0 = (MenuItem) event.getSource();
               TableColumn col0 = (TableColumn) item0.getUserData();
               col0.setVisible(false);
            }
         });

//         id.setStyle("-fx-size:40px;-fx-background-color:#FFFFFF;-fx-border-width:0 0 1 1;" +
//            "-fx-border-color:#CCCCCC");
         view.getColumns().add(id);
      }
   }

   private void buildTableBody(DataGrid grid) {
      ObservableList<List<String>> data = FXCollections.observableArrayList(grid.getDatas());

      for(int i = 0; i < grid.getHeaders().size(); i++) {
         ObservableList<TableColumn> observableList = view.getColumns();
         observableList.get(i).setCellValueFactory(new ArrayValueFactory(self, i));
      }

      view.setItems(data);
   }
}
