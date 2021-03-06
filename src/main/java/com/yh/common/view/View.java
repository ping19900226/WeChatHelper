package com.yh.common.view;

import com.yh.common.request.Callback;
import com.yh.util.Resource;
import com.yh.common.dialog.Alert;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

public abstract class View extends Application {

    public static final int TITLE_HEIGHT = 40;
    public static final int INIT_CONTENT_WIDTH = 800;
    public static final int INIT_CONTENT_HEIGHT = 600;
    public static final int SCROLL_BAR_WIDTH = 15;

    private Stage primaryStage;
    private AnchorPane root;
    private List<View> views;
    private Log log = LogFactory.getLog(View.class);
    private View parentView;
    private View rootView;

    public void open() {
        try {
            start(new Stage());
        }
        catch(Exception e) {
            log.error(e);
            alert("窗口打开失败");
        }
    }

    public void open(Stage stage) {
        try {
            start(stage);
        }
        catch(Exception e) {
            log.error(e);
            alert("窗口打开失败");
        }
    }

    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setWidth(getPrefWidth());
        primaryStage.setMaximized(isMaximized());
        primaryStage.setTitle(getTitle());

        root = new AnchorPane();
        root.setPrefWidth(getPrefWidth() == -1 ? primaryStage.getWidth() : getPrefWidth());
        root.setPrefHeight(getPrefHeight() == -1 ? primaryStage.getHeight() : getPrefHeight());
        root.prefWidthProperty().bind(primaryStage.widthProperty());
        primaryStage.setScene(new Scene(root));
        views = new ArrayList<View>();

        primaryStage.getScene().getStylesheets().add(Resource.getStylePath("style.css"));

        primaryStage.getIcons().add(new Image(Resource.loadImage("logo_b_1.png")));

        start0(root);

//      primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
//         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//            getRoot().setPrefWidth(newValue.doubleValue());
//         }
//      });

        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                getRoot().setPrefHeight(newValue.doubleValue() - TITLE_HEIGHT);
            }
        });

        primaryStage.show();
        afterOpen();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                close();
            }
        });
    }

    public final void openView(View v) {
        openView(v, false);
    }

    public final void openView(View v, boolean newWindow) {
        if(v.unique() && views.contains(v)) {
            v.show();
            return;
        }

        try {
            openView0(v, newWindow);
            views.add(v);
            v.setParentView(this);
        }
        catch(Exception e) {
            log.error(e);
            e.printStackTrace();
            new Alert("窗口打开失败").show();
        }
    }

    public final void close() {

        if(views.size() > 0) {
            Iterator<View> it = views.iterator();

            while(it.hasNext()) {
                View v = it.next();
                v.close();
                it.remove();
            }
        }
        else {
            if(parentView != null) {
                parentView.views.remove(this);
            }

            primaryStage.close();
        }

        afterClose();
    }

    public double getPrefWidth() {
        return INIT_CONTENT_WIDTH;
    }

    public double getPrefHeight() {
        return INIT_CONTENT_HEIGHT;
    }

    public boolean unique() {
        return false;
    }

    public boolean modal() {
        return false;
    }

    public final AnchorPane getRoot() {
        return root;
    }

    public final void show() {
        primaryStage.show();
    }

    public final void setTitle(String title) {
        primaryStage.setTitle(title);
    }

    public void afterOpen() {
        // do nothing
    }

    public void afterClose() {
        // do nothing
    }

    public boolean isMaximized() {
        return true;
    }

    public File showChooseDirDialog() {
        return showChooseDirDialog(null);
    }

    public File showChooseDirDialog(String title) {
        String initDirectory = Resource.loadInfoProperties().getProperty("download.path");
        initDirectory = initDirectory == null ? Resource.downloadPath() : initDirectory;
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle(title == null ? "选择目录" : title);
        dc.setInitialDirectory(new File(initDirectory));
        File dir = dc.showDialog(primaryStage);
        Resource.writeInfoProperties("download.path", dir.getAbsolutePath());
        return dir;
    }

    public File showOpenFileDialog(String title) {
        String initDirectory = Resource.loadInfoProperties().getProperty("download.path");
        initDirectory = initDirectory == null ? Resource.downloadPath() : initDirectory;
        FileChooser fc = new FileChooser();
        fc.setTitle(title == null ? "打开文件" : title);
        fc.setInitialDirectory(new File(initDirectory));
        File dir = fc.showOpenDialog(primaryStage);
        return dir;
    }

    public File showSaveFileDialog(String title) {
        String initDirectory = Resource.loadInfoProperties().getProperty("download.path");
        initDirectory = initDirectory == null ? Resource.downloadPath() : initDirectory;
        FileChooser fc = new FileChooser();
        fc.setTitle(title == null ? "保存文件" : title);
        fc.setInitialDirectory(new File(initDirectory));
        File dir = fc.showSaveDialog(primaryStage);
        return dir;
    }

    public String getId() {
        return this.getClass().getSimpleName();
    }

    @Override public final int hashCode() {
        return getId().hashCode();
    }

    @Override public final boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj instanceof View) {
            View v = (View) obj;

            if(v.getId().equals(this.getId())) {
                return true;
            }
        }

        return false;
    }

    public abstract void start0(AnchorPane root) throws Exception;

    public boolean downloadCheck(String path, String fileName) {
        File file = new File(path);

        if(file.exists()) {
            File[] fs = file.listFiles();
            for(File f : fs) {
                if(f.getName().equals(fileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void alert(String content) {
        alert(null, content, null);
    }

    public void alert(String title, String content, Callback<Byte> callback) {
        openView(new Alert(title, content, callback));
    }

    public void confirm(String content, Callback<Byte> callback) {
        alert(null, content, callback);
    }

    public void confirm(String title, String content, Callback<Byte> callback) {
        openView(new Alert(title, content, callback));
    }

    protected String getTitle() {
        return "新窗口";
    }

    private void openView0(View v, boolean newWindow) throws Exception {
        Stage stage = new Stage();

        if(!newWindow) {
            stage.initOwner(primaryStage.getScene().getWindow());
        }

        v.start(stage);
    }

    private void setParentView(View parentView) {
        this.parentView = parentView;
    }

    private void setRootView(View rootView) {
        this.rootView = rootView;
    }
}