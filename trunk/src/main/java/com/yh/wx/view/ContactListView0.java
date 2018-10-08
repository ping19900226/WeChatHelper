package com.yh.wx.view;

import com.yh.common.request.RequestHandler;
import com.yh.wx.entity.*;
import com.yh.wx.request.WeChatRequestHandler;
import com.yh.wx.request.WechatMessageStore;
import com.yh.common.view.View;
import com.yh.wx.view.component.LabelInput;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

public class ContactListView0 extends View {
   private static final Log log = LogFactory.getLog(ContactListView0.class);
   private WeChatRequestHandler handler = (WeChatRequestHandler) RequestHandler.getRequestHandler(3);
   private AuthInfo info;
   private Contact selectUser;
   private Map<String, Label> labels;
   private Map<String, String> maps;

    @Override
    public void start0(AnchorPane root) throws Exception {
        root.setPadding(new Insets(10));
        info = (AuthInfo) handler.getDefaultAuthenticationInfo().getLoginInfo();
        //handler.getHistoryMessage();

        HBox box = new HBox();
        box.prefHeightProperty().bind(root.prefHeightProperty());
        box.prefWidthProperty().bind(root.prefWidthProperty());
        ScrollPane wrap = new ScrollPane();
        wrap.setPrefHeight(400);
        wrap.prefHeightProperty().bind(box.prefHeightProperty());

        box.getChildren().add(wrap);
        root.getChildren().add(box);
        List<Contact> cs = handler.getContactInitList();
        //List<Contact> cs = handler.getContactList();

        Collections.sort(cs, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getpYQuanPin().compareToIgnoreCase(o2.getpYQuanPin());
            }
        });

        Monitor.get().setContactList(cs);

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

        for(Contact c : cs) {
            final  Label l = new Label();
            initLabel(l, c);

            for(String monitorUserName : Monitor.get().getMonitor()) {
                if(c.getUserName().equals(monitorUserName)) {
                    log.info("Check UUID: " + c.getNickName() + "   " + c.getUserName());

                }
            }

            setOnMouseClick(l, sl, rsArea);
            setContextMenu(l);

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
                        rsArea.appendText(area.getText()+ "\n");

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
                    int c = Integer.parseInt(count.getText());
                    int t = Integer.parseInt(time.getText());

                    for(int i = 0; i < c; i ++) {
                       rsArea.appendText(area.getText());
                       String rs = handler.sendImage(file, selectUser.getUserName());

                       if(rs != null) {
                          rsArea.appendText("图片发送成功\n\t" + rs);
                       } else {
                          rsArea.appendText("-------信息发送失败");
                       }

                       Thread.sleep(t > 1 ? t * 1000 : c > 10 ? c * 100 : 0);
                    }
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

                       for(String uu : Monitor.get().getMonitor()) {
                           if(mesg.getFromUserName().equals(uu)) {
                               AuthInfo info = ((AuthInfo) handler.getDefaultAuthenticationInfo().getLoginInfo());
                               String[] cs = mesg.getContent().split("<br/>");

                               if(cs.length == 1) {
                                   String[] array = new String[2];
                                   array[0] = info.getUserName();
                                   array[1] = cs[0];
                                   cs = array;
                               }

                               if(cs[1].startsWith("开始记录：") || cs[1].startsWith("@" + info.getNickName() + " ")) {
                                   String content = mesg.getContent();
                                   Task task = new Task();
                                   task.setContent(content);
                                   Contact contact = Monitor.get().getContact(mesg.getFromUserName());

                                   task.setMemberCount(contact.getMemberCount());
                                   task.setMemberList(contact.getMemberList());
                                   task.setPublishUserName(cs[0]);
                                   task.setUsername(mesg.getFromUserName());
                                   handler.sendMesg("开始接龙统计", mesg.getFromUserName());
                               }

                               Task task = Monitor.get().getTask(mesg.getFromUserName());

                               if(task != null) {
                                   if(cs[1].equals("收到")) {

                                       List<String> readUser = task.getReadUser();

                                       if(readUser == null) {
                                           readUser = new ArrayList<String>();
                                           task.setReadUser(readUser);
                                       }

                                       readUser.add(cs[0]);
                                   }

                                   if(cs[1].equals("谁收到了")) {
                                       String r = task.getReadUser().size() + "/" + task.getMemberCount() +
                                           "人收到信息：\n";

                                       List<String> allUser = new ArrayList<String>();

                                       for(Map.Entry<String, Contact> entry : task.getMemberList().entrySet()) {
                                           allUser.add(entry.getKey());
                                       }

                                       for(String u : task.getReadUser()) {
                                           Contact c = task.getMemberList().get(u);

                                           if(c == null) {
                                               log.info("User empty");
                                               continue;
                                           }

                                           if(c.getUserName().equals(u)) {
                                               r += c.getDisplayName() == null ? c.getNickName() :
                                                   c.getDisplayName() + "\n";
                                           }
                                       }

                                       allUser.remove(task.getReadUser());

                                       r += "以下人员未收到信息\n";

                                       for(String u : allUser) {
                                           Contact c = task.getMemberList().get(u);

                                           if(c.getUserName().equals(u)) {
                                               r += c.getDisplayName() == null ? c.getNickName() :
                                                   c.getDisplayName() + "\n";
                                           }
                                       }

                                       handler.sendMesg(r, mesg.getFromUserName());
                                   }
                               }
                           }
                       }

                       if(l != null) {
                           l.setStyle(l.getStyle() + "-fx-text-fill: green");
                       }
                   }
                }

                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("Thread error: " + e.getMessage());
            } catch (Exception e) {
               e.printStackTrace();
               log.error("Failed to send message: " + e.getMessage());
            }
        }
    }



    private void initLabel(Label l, Contact c) {
        l.setGraphic(new ImageView(new Image(c.getHeadImgUrl())));
        l.setText(c.getNickName());
        l.setPrefWidth(300);
        l.setUserData(c);
        l.setPrefHeight(30);
        l.setCursor(Cursor.HAND);
        l.setStyle("-fx-background-color: #FFFFFF;-fx-border-width: 0 0 1 0;-fx-border-color: #CCCCCC;");
        labels.put(c.getUserName(), l);
        maps.put(c.getUserName(), c.getNickName());
    }

    private void setOnMouseClick(final Label l, final Label sl, final TextArea rsArea) {
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
    }

    private void setContextMenu(Label l) {
        ContextMenu menu = new ContextMenu();
        MenuItem record = new MenuItem("接龙统计");
        record.setUserData(l.getUserData());
        menu.getItems().add(record);
        addRecordHanlder(record);
        l.setContextMenu(menu);
    }

    private void addRecordHanlder(MenuItem record) {
        record.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact con = (Contact) ((MenuItem)event.getSource()).getUserData();
                Monitor.get().monitor(con.getUserName());

                Contact contact = Monitor.get().getContact(con.getUserName());
                contact = contact == null ? con : contact;
                Monitor.get().addContact(con);

                try {
                    contact.setMemberList(handler.batchGetContact(contact.getUserName(), contact.getMemberList()));
                } catch (Exception e) {
                    log.error("Failed to get member list: " + e.getMessage());
                    Monitor.get().removeTask(contact.getUserName(), null);
                }
            }
        });
    }
}
