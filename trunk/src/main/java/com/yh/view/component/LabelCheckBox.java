package com.yh.view.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class LabelCheckBox extends HBox {

    public LabelCheckBox(String text) {
        label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.prefHeightProperty().bind(this.prefHeightProperty());



        this.getChildren().add(label);

        this.setPadding(new Insets(10));


        this.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(iwp == -1) {
                    iwp = newValue.doubleValue() - lwp;
                }

                label.setPrefWidth(lwp > 1 ? lwp : newValue.doubleValue() * lwp);
            }

        });
    }

    public void setValues(List<Value> values) {
        fields = new CheckBox[values.size()];

        for(int i = 0; i < values.size(); i++) {
            Value value = values.get(i);
            CheckBox field = new CheckBox(value.getName());
            field.setUserData(value);
            field.prefHeightProperty().bind(this.prefHeightProperty());
            field.setMinWidth(50);
            field.setSelected(value.isChecked());
            this.getChildren().add(field);
            fields[i] = field;
        }


    }

    public List<Value> getSelected() {
        List<Value> values = new ArrayList<Value>();
        for(CheckBox field : fields) {
            if(field.isSelected()) {
                Value value = (Value) field.getUserData();
                value.setChecked(true);
                values.add(value);
            }
        }

        return values;
    }

    public void setSelected(String name) {
        for(CheckBox field : fields) {
            if(field.getText().equals(name)) {
                field.setSelected(true);

            }
        }
    }

    public void setLabelWidthP(double val) {
        lwp = val;
    }

    public void setInputWidthP(double val) {
        iwp = val;
    }

    private CheckBox[] fields;
    private Label label;
    private double lwp;
    private double iwp;

    class Value {
        private String value;
        private String name;
        private boolean checked;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
