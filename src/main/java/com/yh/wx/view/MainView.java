package com.yh.wx.view;

import com.yh.Config;
import com.yh.common.view.View;
import com.yh.wx.controller.MainController;
import com.yh.wx.entity.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class MainView extends View {
    private VBox contactPanel;
    private VBox groupPanel;
    private VBox monitorPanel;
    private TabPane controlPanel;
    private MainController controller;

    private List<Contact> contacts;

    public MainView() {
        controller = new MainController();
        contacts = controller.getContactList();
    }

    @Override
    public void start0(AnchorPane root) throws Exception {
        buildMainPanel(root);

        for(Contact contact : contacts) {
            Label label = new YHLabel(contact.getNickName());
            label.setUserData(contact);

            if(contact.getContactFlag() == 0) {
                groupPanel.getChildren().add(label);
            }
            else {
                contactPanel.getChildren().add(label);
            }
        }

        String[] monitors = Config.get().getArray("monitor.list");

        for(String monitor : monitors) {
            Contact monitorContact = ContactCache.get().getContact(monitor);

            if(monitorContact != null) {
                Label label = new YHLabel(monitorContact.getNickName());
                label.setUserData(monitorContact);
                monitorPanel.getChildren().add(label);

                Monitor.get().monitor(monitor);
            }
        }
    }

    @Override
    public double getPrefWidth() {
        return -1;
    }

    @Override
    public double getPrefHeight() {
        return -1;
    }

    private void buildMainPanel(AnchorPane parent) {
        VBox main = new VBox();
        main.setPrefWidth(parent.getPrefWidth());
        main.setPrefHeight(parent.getPrefHeight());
        main.getChildren().add(new YHLabel("控制台"));
        buildContentPanel(main);
        fitParentSize(parent, main);
        parent.getChildren().add(main);
    }

    private void buildContentPanel(VBox parent) {
        SplitPane sp = new SplitPane();
        sp.setDividerPositions(0.3);
        buildLeftPanel(sp);
        buildRightPanel(sp);
        fitParentSize(parent, sp);
        parent.getChildren().add(sp);
    }

    private void buildLeftPanel(SplitPane parent) {
        VBox panel = new VBox();

        Label contactLabel = new YHLabel("联系人");
        panel.getChildren().add(contactLabel);
        buildScrollPanel(contactPanel, panel);

        Label groupLabel = new YHLabel("群组");
        panel.getChildren().add(groupLabel);
        buildScrollPanel(groupPanel, panel);

        fitParentSize(parent, panel);
        parent.getItems().add(panel);
    }

    private void buildScrollPanel(VBox panel, VBox parent) {
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        panel = new VBox();
        sp.setContent(panel);
        fitParentSize(parent, sp);
        parent.getChildren().add(sp);
    }

    private void buildRightPanel(SplitPane parent) {
        VBox panel = new VBox();

        Label monitorLabel = new YHLabel("监测列表");
        panel.getChildren().add(monitorLabel);
        buildScrollPanel(monitorPanel, panel);

        Label controlLabel = new YHLabel("控制台");
        panel.getChildren().add(controlLabel);
        buildControlPanel();
        panel.getChildren().add(controlPanel);
        fitParentSize(panel, controlPanel);

        fitParentSize(parent, panel);

        parent.getItems().add(panel);
    }

    private void buildControlPanel() {
        controlPanel = new TabPane();
        controlPanel.getTabs().add(addTab("全部"));
    }

    private Tab addTab(String name) {
        Tab tab = new Tab(name);
        VBox box = new VBox();
        tab.setContent(new ScrollPane(box));
        return tab;
    }

    private void fitParentSize(Region parent, Region sub) {
        fitParentWidth(parent, sub);
        fitParentHeight(parent, sub);

    }

    private void fitParentWidth(Region parent, Region sub) {
        //sub.widthProperty().bind(parent.widthProperty());
        sub.prefWidthProperty().bind(parent.prefWidthProperty());
        sub.maxWidthProperty().bind(parent.maxWidthProperty());
        sub.minWidthProperty().bind(parent.minWidthProperty());
    }

    private void fitParentHeight(Region parent, Region sub) {
        //sub.heightProperty().bind(parent.heightProperty());
        sub.prefHeightProperty().bind(parent.prefHeightProperty());
        sub.maxHeightProperty().bind(parent.maxHeightProperty());
        sub.minHeightProperty().bind(parent.minHeightProperty());
    }

    private void fitParentHeight(Region parent, Region sub, double subtractNum, double divideNum) {
        //sub.heightProperty().bind(parent.heightProperty());
//        sub.setMinHeight((parent.getMinHeight() - subtractNum) / divideNum);
//        sub.setPrefHeight((parent.getPrefHeight() - subtractNum) / divideNum);
//        sub.setMaxHeight((parent.getMinWidth() - subtractNum) / divideNum);
//        sub.prefHeightProperty().bind(parent.prefHeightProperty().subtract(subtractNum).divide(divideNum));
//        sub.maxHeightProperty().bind(parent.maxHeightProperty().subtract(subtractNum).divide(divideNum));
//        sub.minHeightProperty().bind(parent.minHeightProperty().subtract(subtractNum).divide(divideNum));
    }

    class YHLabel extends Label {
        private YHLabel() {

        }

        public YHLabel(String text) {
            super(text);
            this.setWidth(Region.USE_COMPUTED_SIZE);
            this.setHeight(50);
            this.setPrefHeight(50);
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 0, 10, 10));
        }
    }

    @Override
    protected String getTitle() {
        return "控制台";
    }
}
