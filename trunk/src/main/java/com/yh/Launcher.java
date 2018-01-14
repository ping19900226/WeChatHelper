package com.yh;

import com.yh.request.*;
import com.yh.view.WorkbenchView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.CookieHandler;
import java.net.URLConnection;

public class Launcher extends Application {
   @Override
   public void start(Stage primaryStage) throws Exception {
      System.setProperty ("jsse.enableSNIExtension", "false");
      URLConnection.setContentHandlerFactory(new YHContentHandlerFactory());
      CookieHandler.setDefault(RequestHelper.getCookieHander());
      RequestHandler.regist(new BugzillaRequestHandler());
      RequestHandler.regist(new ReviewBoardRequestHandler());
      RequestHandler.regist(new WeChatRequestHandler());
      new WorkbenchView().start(new Stage());
   }

   public static void main(String[] args) {
      launch(args);
   }
}
