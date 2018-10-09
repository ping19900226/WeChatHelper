package com.yh.wx.view;

import com.yh.common.request.Callback;
import com.yh.common.view.View;
import com.yh.wx.controller.MainController;
import com.yh.wx.entity.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;

public class MainView extends View {
    private VBox contactPanel;
    private VBox groupPanel;
    private VBox monitorPanel;
    private TabPane controlPanel;
    private MainController controller;
    private SplitPane sp;
    private List<Contact> contacts;

    public MainView() {
        controller = new MainController();
    }

    @Override
    public void start0(AnchorPane root) throws Exception {
        buildMainPanel(root);

        refreshContactList();

        controller.startProcess(new Callback<Message>() {
            @Override
            public void call(Message message) {
                //System.out.println(message.getContent());
            }
        });

        sp.setDividerPositions(0.2);
    }

    @Override
    public double getPrefWidth() {
        return -1;
    }

    @Override
    public double getPrefHeight() {
        return -1;
    }

    @Override
    protected String getTitle() {
        return "控制台";
    }

    private void buildMainPanel(AnchorPane parent) {
        VBox main = new VBox();
        main.setPrefWidth(parent.getPrefWidth());
        main.setPrefHeight(parent.getPrefHeight());
        main.getChildren().add(new YHLabel(main, "控制台"));
        buildContentPanel(main);
        fitParentSize(parent, main);
        parent.getChildren().add(main);
    }

    private void buildContentPanel(VBox parent) {
        sp = new SplitPane();
        buildLeftPanel(sp);
        buildRightPanel(sp);
        fitParentSize(parent, sp);
        sp.setDividerPositions(0.2);
        parent.getChildren().add(sp);
    }

    private void buildLeftPanel(SplitPane parent) {
        VBox panel = new VBox();
        contactPanel = new VBox();
        groupPanel = new VBox();

        Label contactLabel = new YHLabel(panel, "联系人");
        panel.getChildren().add(contactLabel);

        buildScrollPanel(contactPanel, panel);

        Label groupLabel = new YHLabel(panel, "群组");
        panel.getChildren().add(groupLabel);
        buildScrollPanel(groupPanel, panel);

        fitParentSize(parent, panel);
        parent.getItems().add(panel);
    }

    private void buildScrollPanel(VBox panel, VBox parent) {
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setContent(panel);
        fitParentSize(parent, sp);
        parent.getChildren().add(sp);
    }

    private void buildRightPanel(SplitPane parent) {
        VBox panel = new VBox();
        monitorPanel = new VBox();

        Label monitorLabel = new YHLabel(panel, "监测列表");
        panel.getChildren().add(monitorLabel);
        buildScrollPanel(monitorPanel, panel);

        Label controlLabel = new YHLabel(panel, "控制台");
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
        sub.prefWidthProperty().bind(parent.prefWidthProperty());
        sub.maxWidthProperty().bind(parent.maxWidthProperty());
        sub.minWidthProperty().bind(parent.minWidthProperty());
    }

    private void fitParentHeight(Region parent, Region sub) {
        sub.prefHeightProperty().bind(parent.prefHeightProperty());
        sub.maxHeightProperty().bind(parent.maxHeightProperty());
        sub.minHeightProperty().bind(parent.minHeightProperty());
    }

    private void onConcatItemContextMenu(Label label) {
        ContextMenu menu = new ContextMenu();
        label.setContextMenu(menu);

        MenuItem record = new MenuItem("记录");
        menu.getItems().add(record);
        MenuItem refresh = new MenuItem("刷新");
        menu.getItems().add(refresh);
        refresh.setOnAction(new RefreshContactListListener());
    }

    private void refreshContactList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contacts = controller.getContactList();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        groupPanel.getChildren().clear();
                        contactPanel.getChildren().clear();
                    }
                });

                for(Contact contact : contacts) {
                    YHLabel label = new YHLabel(contact.getNickName());
                    label.setUserData(contact);
                    label.hover();
                    onConcatItemContextMenu(label);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(contact.getContactFlag() == 0) {
                                fitParentWidth(groupPanel, label);
                                groupPanel.getChildren().add(label);
                            }
                            else {
                                fitParentWidth(contactPanel, label);
                                contactPanel.getChildren().add(label);
                            }
                        }
                    });
                }

//                String[] monitors = Config.get().getArray("monitor.list");
//
//                for(String monitor : monitors) {
//                    Contact monitorContact = ContactCache.get().getContact(monitor);
//
//                    if(monitorContact != null) {
//
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                Label label = new YHLabel(monitorContact.getNickName());
//                                label.setUserData(monitorContact);
//                                monitorPanel.getChildren().add(label);
//                            }
//                        });
//
//                        Monitor.get().monitor(monitor);
//                    }
//                }
            }
        }).start();
    }

    class YHLabel extends Label {
        private YHLabel() {

        }

        public YHLabel(String text) {
            super(text);
            this.setHeight(50);
            this.setPrefHeight(50);
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(5, 0, 5, 5));
            this.setStyle("-fx-background-color: #FFFFFF");
        }

        public YHLabel(Region parent, String text) {
            this(text);
            fitParentWidth(parent, this);
        }

        public void hover() {
            this.setCursor(Cursor.HAND);
            this.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ((YHLabel) event.getSource()).setStyle("-fx-background-color:#EFEFEF");
                }
            });

            this.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ((YHLabel) event.getSource()).setStyle("-fx-background-color:#FFFFFF");
                }
            });
        }
    }

    class RefreshContactListListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            refreshContactList();
        }
    }
}
