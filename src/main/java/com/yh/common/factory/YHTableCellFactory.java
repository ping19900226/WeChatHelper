package com.yh.common.factory;

import com.yh.view.ExplorerView;
import com.yh.common.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.List;

public class YHTableCellFactory<T> implements Callback<TableColumn<List<String>, T>, TableCell<List<String>, T>> {

   public YHTableCellFactory(View context, int index, List<T> values) {
      this.context = context;
      this.index = index;
      this.values = values;
   }

   @Override
   public TableCell call(TableColumn param) {
      TableCell cell = new TableCell();
      cell.setPrefHeight(40);
      cell.setText(values == null ? "" : values.get(index).toString());
      cell.setUserData(index);

      cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
            TableCell cell0 = (TableCell)event.getSource();
            if(event.getButton() == MouseButton.PRIMARY &&
               event.getClickCount() == 2) {
               TableRow v = (TableRow) event.getSource();
               String url = "http://192.168.1.120//show_bug.cgi?id=" + v.getUserData().toString();
               context.openView(new ExplorerView(url));

            }
            if(event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY) {
               ContextMenu menu = new ContextMenu();
               MenuItem item = new MenuItem("隐藏");
               item.setUserData(cell0);
               menu.getItems().add(item);
               Object index = cell0.getUserData();

               if(index != null) {
                  TableView table = cell0.getTableView();

                  for(Object obj : table.getColumns()) {
                     TableColumn col0 = ((TableColumn)obj);
                     String style = col0.getStyle();
                     style = style == null ? style : style.replace
                        ("-fx-background-color:rgba(200,200,200, .4);", "");
                     col0.setStyle(style);
                  }

                  TableColumn col = (TableColumn) table.getColumns().get
                     (Integer.parseInt(index.toString()));
                  col.setStyle("-fx-background-color:rgba(200,200,200, .4);");
               }


               item.setOnAction(new EventHandler<ActionEvent>() {

                  @Override
                  public void handle(ActionEvent event) {
                     MenuItem item0 = (MenuItem) event.getSource();
                     TableCell cell1 = (TableCell) item0.getUserData();
                     Object index = cell1.getUserData();

                     if(index != null) {
                        TableColumn col = (TableColumn) cell1.getTableView().getColumns().get
                           (Integer.parseInt(index.toString()));
                        col.setVisible(false);
                     }

                  }
               });

               menu.show(cell0, event.getScreenX(),
                  event.getScreenY());
            }
         }
      });

      return cell;
   }

   private int index;
   private List<T> values;
   private View context;
}
