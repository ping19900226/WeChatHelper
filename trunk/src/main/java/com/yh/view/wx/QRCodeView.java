package com.yh.view.wx;

import com.yh.request.Callback;
import com.yh.request.RequestHandler;
import com.yh.request.WeChatRequestHandler;
import com.yh.view.View;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;

public class QRCodeView extends View {
    private WeChatRequestHandler handler = (WeChatRequestHandler) RequestHandler.getRequestHandler(3);
    private static final Log log = LogFactory.getLog(QRCodeView.class);

    @Override
    public void start0(AnchorPane root) throws Exception {
        VBox imageBox = new VBox();
        root.getChildren().add(imageBox);
        final ImageView image = new ImageView();
        image.setFitWidth(300);
        image.setFitHeight(300);
        final Label l = new Label();
        l.setPrefWidth(300);
        l.setPrefHeight(30);
        l.setAlignment(Pos.CENTER);
        l.setText("正在等待扫码");
        imageBox.getChildren().addAll(image, l);

        try {
            handler.getMain();
            String appid = handler.loadJs();
            final String uuid = handler.jsLogin(appid);

            log.info("Get qrcode.");

            handler.getQrcode(uuid, new Callback<InputStream>() {

                @Override
                public void call(InputStream inputStream) {
                    image.setImage(new Image(inputStream));
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String res = null;

                    try {

                        while(res == null) {
                            res = handler.login(uuid);
                        }

                        handler.newLoginPage(res);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    l.setText("扫描完毕");
                                    new ContactListView().start(new Stage());
                                    close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPrefWidth() {
        return 300;
    }

    @Override
    public int getPrefHeight() {
        return 130;
    }
}
