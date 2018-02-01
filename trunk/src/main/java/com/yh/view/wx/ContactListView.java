package com.yh.view.wx;

import com.yh.request.RequestHandler;
import com.yh.request.WeChatRequestHandler;
import com.yh.request.WechatMessageStore;
import com.yh.request.entity.wx.AuthInfo;
import com.yh.request.entity.wx.Contact;
import com.yh.request.entity.wx.Message;
import com.yh.view.View;
import com.yh.view.component.LabelInput;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.*;

public class ContactListView extends View {
   private WeChatRequestHandler handler = (WeChatRequestHandler) RequestHandler.getRequestHandler(3);
   private AuthInfo info;
   private Contact selectUser;
   private Map<String, Label> labels;
   private Map<String, String> maps;

    @Override
    public void start0(AnchorPane root) throws Exception {
        root.setPadding(new Insets(10));
        info = (AuthInfo) handler.getDefaultAuthenticationInfo().getLoginInfo();

        HBox box = new HBox();
        box.prefHeightProperty().bind(root.prefHeightProperty());
        box.prefWidthProperty().bind(root.prefWidthProperty());
        ScrollPane wrap = new ScrollPane();
        wrap.setPrefHeight(400);
        wrap.prefHeightProperty().bind(box.prefHeightProperty());

        box.getChildren().add(wrap);
        root.getChildren().add(box);
        handler.getContactInitList();
        List<Contact> cs = handler.getContactList();

        Collections.sort(cs, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getpYQuanPin().compareToIgnoreCase(o2.getpYQuanPin());
            }
        });

        VBox userList = new VBox();
        wrap.setContent(userList);

        final Label sl = new Label();
        VBox inputArea = new VBox();
        inputArea.getChildren().add(sl);
        inputArea.setPrefWidth(getPrefWidth() - 300);
        inputArea.prefHeightProperty().bind(box.prefHeightProperty());
        box.getChildren().add(inputArea);

        final TextArea area = new TextArea();
        area.prefWidthProperty().bind(inputArea.prefWidthProperty());
        inputArea.getChildren().add(area);

        final LabelInput count = new LabelInput("发送次数");
        count.prefWidthProperty().bind(inputArea.prefWidthProperty());
        count.setLabelWidthP(80);
        inputArea.getChildren().add(count);

       final LabelInput time = new LabelInput("时间间隔");
       time.prefWidthProperty().bind(inputArea.prefWidthProperty());
       time.setLabelWidthP(80);
       inputArea.getChildren().add(time);

        HBox buttonPane = new HBox();

        Button send = new Button("发送");
        buttonPane.getChildren().add(send);
        Button sendImage = new Button("发送图片");
        buttonPane.getChildren().add(sendImage);
        inputArea.getChildren().add(buttonPane);

        final TextArea rsArea = new TextArea();
        rsArea.prefWidthProperty().bind(inputArea.prefWidthProperty());
        inputArea.getChildren().add(rsArea);

        labels =new HashMap<String, Label>(cs.size());
        maps = new HashMap<String, String>(cs.size());

        for (Contact c : cs) {
           final  Label l = new Label();
            l.setGraphic(new ImageView(new Image(c.getHeadImgUrl())));
            l.setText(c.getNickName().replace("<span>[.*]</span>", ""));
            l.setPrefWidth(300);
            l.setUserData(c);
            l.setPrefHeight(30);
            l.setCursor(Cursor.HAND);
            l.setStyle("-fx-background-color: #FFFFFF;-fx-border-width: 0 0 1 0;-fx-border-color: #CCCCCC;");
            labels.put(c.getUserName(), l);
            maps.put(c.getUserName(), c.getNickName());

            l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Contact con = (Contact) ((Label)event.getSource()).getUserData();
                    selectUser = con;
                    sl.setText(con.getNickName());
                    String style = l.getStyle();

                    if(style.contains("-fx-text-fill: green")) {
                        l.setStyle(style.replace("-fx-text-fill: green", ""));
                    }

                    List<Message> mesgs = WechatMessageStore.getMessage(con.getUserName());

                    if(mesgs != null) {
                        for(Message mesg : mesgs) {
                            rsArea.appendText(maps.get(mesg.getFromUserName()) + " : " + mesg.getContent() + "\n");
                        }
                    }
                }
            });

            userList.getChildren().add(l);
        }

        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int c = Integer.parseInt(count.getText());
                    int t = Integer.parseInt(time.getText());

                    for(int i = 0; i < c; i ++) {
                        boolean res = handler.sendMesg(area.getText(), selectUser.getUserName());
                        if(res) {

                        }
                        rsArea.appendText(area.getText());

                        Thread.sleep(t > 1 ? t * 1000 : c > 10 ? c * 100 : 0);
                    }
                    area.clear();

                } catch (Exception e) {
                    rsArea.appendText("-------信息发送失败");
                }
            }
        });

        sendImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    File file = showOpenFileDialog("选择一张图片");
                    String rs = handler.sendImage(file, selectUser.getUserName());
                    if(rs != null) {
                        rsArea.appendText("图片发送成功\n\t" + rs);
                        return;
                    }

                    rsArea.appendText("-------信息发送失败");
                } catch (Exception e) {
                    rsArea.appendText("-------信息发送失败");
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                messageListener();
            }
        }).start();

    }

    public void messageListener() {
        while (true) {
            try {
                if(handler.isHasMessage()) {
                   List<Message> mesgs = handler.webwxsync();

                   for(Message mesg : mesgs) {
                       Label l = labels.get(mesg.getFromUserName());
                       if(l != null) {
                           l.setStyle(l.getStyle() + "-fx-text-fill: green");
                       }
                   }
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
