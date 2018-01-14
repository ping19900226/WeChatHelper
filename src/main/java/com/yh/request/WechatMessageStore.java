package com.yh.request;

import com.yh.request.entity.Message;
import com.yh.util.JSONUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class WechatMessageStore {
    private static final String DATA_PATH = System.getProperty("user.home") + System.getProperty("file.separator") +
       "YH" + System.getProperty("file.separator") + "wx" + System.getProperty("file.separator")
       + "data" + System.getProperty("file.separator");
    private static final String HEAD_IMAGE_PATH = System.getProperty("user.home") + System.getProperty("file.separator") +
       "YH" + System.getProperty("file.separator") + "wx" + System.getProperty("file.separator")
       + "headimage" + System.getProperty("file.separator");
    private static final String IMAGE_PATH = System.getProperty("user.home") + System.getProperty("file.separator") +
       "YH" + System.getProperty("file.separator") + "wx" + System.getProperty("file.separator")
       + "image" + System.getProperty("file.separator");

    static {
        File dataDir = new File(DATA_PATH);

        if(!dataDir.exists()) {
            dataDir.mkdirs();
            File[] fs = dataDir.listFiles();

            for(File f: fs) {
                f.delete();
            }
        }

        File hImgDir = new File(HEAD_IMAGE_PATH);

        if(!hImgDir.exists()) {
            hImgDir.mkdirs();
        }

        File imgDir = new File(IMAGE_PATH);

        if(!imgDir.exists()) {
            imgDir.mkdirs();
        }

    }

    public static void storeMessage(Message message) {
        FileWriter writer = null;

        File file = new File(DATA_PATH + message.getFromUserName());

        try {
            writer = new FileWriter(file, true);
            writer.write(JSONUtil.toJSONString(message) + "\n");
            writer.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Message> getMessage(String fromUserName) {
        BufferedReader reader = null;
        File f = new File(DATA_PATH + fromUserName);

        try {
            if(!f.exists()) {
                return null;
            }

            reader = new BufferedReader(new FileReader(f));
            List<Message> lines = new ArrayList<Message>();
            String line = "";
            boolean content = false;

            while((line = reader.readLine()) != null) {
                Message mesg = JSONUtil.parse(line.replace("\n", ""), Message.class);
                lines.add(mesg);
            }
            return lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void storeImage(String url) {

    }
}
