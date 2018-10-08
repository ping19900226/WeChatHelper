package com.yh.wx.view;

import com.yh.common.request.Callback;
import com.yh.common.request.RequestHandler;
import com.yh.exception.YHException;
import com.yh.exception.YHRTException;
import com.yh.wx.request.WeChatRequestHandler;
import com.yh.common.view.View;
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
    private int loginCount = 0;
    private Label textLabel;
    private ImageView qrCodeView;

    @Override
    public void start0(AnchorPane root) throws Exception {
        VBox imageBox = new VBox();
        root.getChildren().add(imageBox);
        qrCodeView = new ImageView();
        qrCodeView.setFitWidth(300);
        qrCodeView.setFitHeight(300);
        textLabel = new Label();
        textLabel.setPrefWidth(300);
        textLabel.setPrefHeight(30);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setText("正在等待扫码");
        imageBox.getChildren().addAll(qrCodeView, textLabel);

        final String uuid = getUUID();
        login(uuid);
    }

    @Override
    public double getPrefWidth() {
        return 300;
    }

    @Override
    public double getPrefHeight() {
        return 130;
    }

    private String getUUID() throws Exception {
        handler.getMain();
        String appid = handler.loadJs();
        String uuid = handler.jsLogin(appid);
        return uuid;
    }

    private void login(String uuid) {
        try {
            login0(uuid);
        }
        catch(Exception e) {
            if(loginCount == 10) {
                alert("登录失败");
                log.error("Failed to login, with count 10");
                return;
            }

            loginCount ++;
            log.info("Last login failed, try to the " + loginCount + "th login.");
            login(uuid);
        }
    }

    private void login0(final String uuid) throws Exception {
        getQrCode(uuid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkScan(uuid);
                }
                catch(Exception e) {
                    log.error("Check scan error." + e.getMessage());
                    throw new YHRTException(e);
                }
            }
        }).start();
    }

    private void getQrCode(String uuid) throws YHException {
        handler.getQrcode(uuid, new Callback<InputStream>() {

            @Override
            public void call(InputStream inputStream) {
                qrCodeView.setImage(new Image(inputStream));
            }
        });
    }

    private void checkScan(String uuid) throws Exception {
        String res = null;

        while(res == null) {
            res = handler.login(uuid);
        }

        handler.newLoginPage(res);
        toContactListView();
    }

    private void toContactListView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    textLabel.setText("扫描完毕");
                    new MainView().start(new Stage());
                    close();
                } catch (Exception e) {
                    log.error("Failed to open contact list view.", e);
                    alert("打开主页面失败");
                }

            }
        });
    }
}
