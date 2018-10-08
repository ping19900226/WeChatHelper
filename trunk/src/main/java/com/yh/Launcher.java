package com.yh;

import com.yh.common.request.RequestHandler;
import com.yh.common.request.RequestHelper;
import com.yh.request.*;
import com.yh.util.Resource;
import com.yh.view.WorkbenchView;
import com.yh.wx.request.WeChatRequestHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.net.CookieHandler;
import java.net.URLConnection;

public class Launcher extends Application {
   @Override
   public void start(Stage primaryStage) throws Exception {
      System.setProperty ("jsse.enableSNIExtension", "false");
      URLConnection.setContentHandlerFactory(new YHContentHandlerFactory());
      CookieHandler.setDefault(RequestHelper.getCookieHander());
      checkAndCreateFolder();
      RequestHandler.regist(new BugzillaRequestHandler());
      RequestHandler.regist(new ReviewBoardRequestHandler());
      RequestHandler.regist(new WeChatRequestHandler());
      new WorkbenchView().start(new Stage());
   }

   private void checkAndCreateFolder() {
      File confDir = new File(Resource.documentPath().concat(File.separator).concat("conf"));

      if(!confDir.exists()) {
         confDir.mkdirs();
      }

      String infoFile = confDir.getAbsolutePath() + File.separator + "default.info";

      File downloadDir = new File(Resource.downloadPath());

      if(!downloadDir.exists()) {
         downloadDir.mkdirs();
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
