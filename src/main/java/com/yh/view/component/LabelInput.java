package com.yh.view.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class LabelInput extends HBox{

    public LabelInput(String text) {
        label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.prefHeightProperty().bind(this.prefHeightProperty());

        field = new TextField();
        field.prefHeightProperty().bind(this.prefHeightProperty());
        field.setMinWidth(50);

        this.getChildren().add(label);
        this.getChildren().add(field);
        this.setPadding(new Insets(10));


        this.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(iwp == -1) {
                    iwp = newValue.doubleValue() - lwp;
                }

                label.setPrefWidth(lwp > 1 ? lwp : newValue.doubleValue() * lwp);
                field.setPrefWidth(iwp > 1 ? iwp : newValue.doubleValue() * iwp);
            }

        });
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String val) {
        field.setText(val);
    }

    public void setLabelWidthP(double val) {
        lwp = val;
    }

    public void setInputWidthP(double val) {
        iwp = val;
    }

    private TextField field;
    private Label label;
    private double lwp;
    private double iwp;
}
