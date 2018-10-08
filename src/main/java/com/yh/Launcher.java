package com.yh;

import com.yh.common.request.RequestHandler;
import com.yh.common.request.RequestHelper;
import com.yh.request.*;
import com.yh.util.Resource;
import com.yh.view.WorkbenchView;
import com.yh.wx.request.WeChatRequestHandler;
import com.yh.wx.view.QRCodeView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.CookieHandler;
import java.net.URLConnection;

public class Launcher extends Application {
    private Log log = LogFactory.getLog(Launcher.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
//      System.setProperty ("jsse.enableSNIExtension", "false");
//      URLConnection.setContentHandlerFactory(new YHContentHandlerFactory());
//      CookieHandler.setDefault(RequestHelper.getCookieHander());
//      checkAndCreateFolder();
//      RequestHandler.regist(new BugzillaRequestHandler());
//      RequestHandler.regist(new ReviewBoardRequestHandler());
//      RequestHandler.regist(new WeChatRequestHandler());
//      new WorkbenchView().start(new Stage());

        System.setProperty("jsse.enableSNIExtension", "false");
        RequestHandler.regist(new WeChatRequestHandler());
        load();
        new QRCodeView().start(primaryStage);
    }

    private void load() {
        File confDir = new File((System.getProperty("user.home") + File.separator + "YH")
            .concat(File.separator).concat("conf"));
        String infoFile = confDir.getAbsolutePath() + File.separator + "default.conf";
        log.info("config file: " + infoFile);
        Config.get().load(infoFile);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
