package com.yh.view;

import com.yh.core.DownloadListener;
import com.yh.core.YHFileUtil;
import javafx.beans.value.*;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;

public class ExplorerView extends View{

   public ExplorerView() {

   }

   public ExplorerView(String url) {
      this.url = url;
   }

   public void start0(AnchorPane root) throws Exception {

      view = new WebView();
      engine = view.getEngine();
      engine.load(url);
      root.getChildren().add(view);

      root.widthProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            view.setPrefWidth(newValue.doubleValue());
         }
      });

      root.heightProperty().addListener(new ChangeListener<Number>() {
         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            view.setPrefHeight(newValue.doubleValue());
         }
      });

      handle();
   }

   public WebEngine getEngine() {
      return engine;
   }

   private void handle() {
      engine.getLoadWorker().titleProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            System.out.println(newValue);
            setTitle(newValue);
         }
      });

      engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
         @Override
         public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
            if(newValue == Worker.State.SUCCEEDED) {
               setTitle(view.getEngine().getTitle());
            }
            else if (newValue == Worker.State.CANCELLED) {
               //may be it is not a html, to download
               final Download dl = new Download(engine.getLocation());

               if(dl.canDownload()) {
                  if(engine.getHistory().getCurrentIndex() > 0) {
                     engine.getHistory().go(-1);
                  }

                  File dir = showChooseDirDialog();

                  dl.download(dir.getAbsolutePath(), new DownloadListener() {
                     @Override
                     public void download(int progress) {
                        System.out.println(progress + "   " + dl.getContentLength());
                        if(progress >= dl.getContentLength()) {
                           new Alert(Alert.AlertType.INFORMATION,"下载完成").show();
                        }
                     }
                  });
               }

            }
         }
      });

      engine.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
         @Override
         public WebEngine call(PopupFeatures param) {
            ExplorerView view = new ExplorerView();
            view.open();
            return view.getEngine();
         }
      });
   }

   class Download {

      public Download(String url) {
         this.url = url;
         load();
      }

      public boolean canDownload() {
         return canDownload;
      }

      public int getContentLength() {
         return contentLength;
      }

      public void download(String dir, DownloadListener listener) {
         YHFileUtil.downloadFile(dir, stream, filename, listener);
      }

      private void load() {
         try {
            URL durl = new URL(url);
            URLConnection connection = durl.openConnection();
            connection.connect();

            String type = connection.getContentType();
            String ft = type.split(";")[0];
            filename = type.contains(";") ? type.split(";")[1] : "";

            if(filename.equals("")) {
               url = url.replace("\\", "/");
               filename = url.substring(url.lastIndexOf("/"), url.length());
            }
            else {
               filename = filename.trim().replace("name=", "").replace("\"", "");
               filename = URLDecoder.decode(filename, Charset.forName("utf-8").displayName());
            }

            System.out.println(ft + "---" + filename);

            Map h = connection.getHeaderFields();
            connection.getHeaderField("Content-disposition");
            contentLength = connection.getContentLength();

            if(ft.equalsIgnoreCase("application/octet-stream")) {
               canDownload = true;
               stream = connection.getInputStream();
            }
         }
         catch(MalformedURLException e) {
            log.error(e);
            canDownload = false;
         }
         catch(IOException e) {
            log.error(e);
            canDownload = false;
         }
      }

      private String url;
      private boolean canDownload;
      private InputStream stream;
      private String filename;
      private int contentLength;
   }

   private static final Log log = LogFactory.getLog(ExplorerView.class);
   private String url;
   private WebView view;
   private WebEngine engine;;
}
