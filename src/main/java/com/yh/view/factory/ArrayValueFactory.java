package com.yh.view.factory;

import com.yh.view.View;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;

public class ArrayValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<List<S>,T>,
   ObservableValue<T>> {

   public ArrayValueFactory(View context, int index) {
      this.index = index;
      this.context = context;
   }

   public int getIndex() {
      return index;
   }

   @Override
   public ObservableValue<T> call(TableColumn.CellDataFeatures<List<S>, T> param) {
      List<S> items = param.getValue();
      S value = items.get(index);
      //param.getTableColumn().setCellFactory(new YHTableCellFactory(context, index, items));

      if (value instanceof ObservableValue) {
         return (ObservableValue)value;
      }

      if (value instanceof Boolean) {
         return (ObservableValue<T>) new ReadOnlyBooleanWrapper((Boolean)value);
      } else if (value instanceof Integer) {
         return (ObservableValue<T>) new ReadOnlyIntegerWrapper((Integer)value);
      } else if (value instanceof Float) {
         return (ObservableValue<T>) new ReadOnlyFloatWrapper((Float)value);
      } else if (value instanceof Long) {
         return (ObservableValue<T>) new ReadOnlyLongWrapper((Long)value);
      } else if (value instanceof Double) {
         return (ObservableValue<T>) new ReadOnlyDoubleWrapper((Double)value);
      } else if (value instanceof String) {
         return (ObservableValue<T>) new ReadOnlyStringWrapper((String)value);
      }

      return new ReadOnlyObjectWrapper<T>((T)value);
   }

   private int index;
   private View context;
}
