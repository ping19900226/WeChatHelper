package com.yh.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yh.request.entity.wx.AuthInfo;
import com.yh.request.entity.wx.Contact;
import com.yh.request.entity.FinalData;
import com.yh.request.entity.wx.Error;
import com.yh.request.entity.wx.Message;
import com.yh.util.JSONUtil;
import com.yh.util.RandomUtil;
import com.yh.util.XMLUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeChatRequestHandler extends RequestHandler {

    public WeChatRequestHandler() {
        setDefaultAuthenticationInfo(new WeChatAuthenticationInfo());
    }

    public void login(String username, String password) {

    }

    public void getMain() throws Exception {
        get("https://wx.qq.com", null);
    }

    public String jsLogin(String appid){
        String rs = getContent("https://login.wx.qq.com/jslogin?appid=" + appid
                + "&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_="
                + System.currentTimeMillis());

        rs = rs.substring(rs.indexOf("\"")  + 1);
        rs = rs.substring(0, rs.indexOf("\""));
        return rs;
    }

    public String login(String uuid) throws Exception {
        final FinalData<String> data = new FinalData<String>();

        get("https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=" + uuid + "&tip=0&r=" +
                RandomUtil.getNumber(10) + "&_=" + System.currentTimeMillis(), new ResponseHandler() {
            @Override
            public void handle(Response response) throws Exception {
                if(response.getStatusCode() == 200) {
                    String rs = response.getStringContent();
                    String code = rs.substring(rs.indexOf("window.code=") + "window.code=".length(), rs.indexOf(";"));

                    if("200".equals(code)) {
                        rs = rs.substring(rs.indexOf("window.redirect_uri=\"") + "window.redirect_uri=\"".length());
                        rs = rs.substring(0, rs.indexOf("\""));
                        rs = rs + "&fun=new&version=v2";
                        data.setValue(rs);
                    }
                }
            }
        });

        return data.getValue();
    }

    public String loadJs() throws Exception {
        String js = getContent("https://res.wx.qq.com/a/wx_fed/webwx/res/static/js/index_e01fd8a.js");
        js = js.substring(js.indexOf("API_jsLogin:") + "API_jsLogin:".length());
        js = js.substring(js.indexOf("?appid=") + "?appid=".length(), js.indexOf("&"));
        return js;
    }

    public void getQrcode(String uuid, final Callback<InputStream> callback) {
        try {
            get("https://login.weixin.qq.com/qrcode/" + uuid, new ResponseHandler() {
                @Override
                public void handle(Response response) throws Exception {
                    callback.call(response.getContent());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeviceId() {
        String n =  RandomUtil.getFixedNumber(15);
System.out.println(n);
        return "e" + ("" + n).substring(2, 17);
    }

    public void newLoginPage(String url) throws Exception {
        String xml = getContent(url);
        System.out.println(xml);
        Error info = XMLUtil.xmlToObject(xml, Error.class);

        info.setDeceiveId(getDeviceId());
        getDefaultAuthenticationInfo().login();
        getDefaultAuthenticationInfo().setLoginInfo(info);
    }

    public List<Contact> getContactList() {
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
        String content = getContent("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?r=1" +  + System.currentTimeMillis() + "&seq=0&skey=" + info.getSkey());

        List<Contact> data = new ArrayList<Contact>();
        JSONObject resp = JSON.parseObject(content);
        if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
            AuthInfo infpo =  ((AuthInfo) getDefaultAuthenticationInfo().getLoginInfo());
            JSONArray contactList = resp.getJSONArray("MemberList");

            for (int i = 0; i < contactList.size(); i++) {
                JSONObject contact = contactList.getJSONObject(i);
                Contact cnc = new Contact();
                data.add(cnc);
                cnc.setUserName(contact.getString("UserName"));
                cnc.setNickName(contact.getString("NickName"));
                cnc.setHeadImgUrl("https://wx.qq.com" + contact.getString("HeadImgUrl"));
                cnc.setContactFlag(contact.getIntValue("ContactFlag"));
                cnc.setpYQuanPin(contact.getString("PYQuanPin"));
                cnc.setpYInitial(contact.getString("PYInitial"));
                cnc.setRemarkName(contact.getString("RemarkName"));
                cnc.setMenberCount(contact.getInteger("MemberCount"));
                cnc.setSex(contact.getIntValue("Sex"));
            }
        }

        return data;
    }

    public List<Contact> getContactInitList() throws Exception {
        final AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
        final List<Contact> data = new ArrayList<Contact>();
        JSONObject message = new JSONObject();
        JSONObject baseRequest = new JSONObject();
        baseRequest.put("Uin", info.getWxuin());
        baseRequest.put("Sid", info.getWxsid());
        baseRequest.put("Skey", info.getSkey());

        message.put("BaseRequest", baseRequest);

        post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=1182666740&lang=zh_CN&pass_ticket=" +
                info.getPass_ticket(), message.toJSONString(), new ResponseHandler() {
            @Override
            public void handle(Response response) throws Exception {
                String text = response.getStringContent();
                JSONObject resp = JSON.parseObject(text);

                if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
                    AuthInfo infpo =  ((AuthInfo) getDefaultAuthenticationInfo().getLoginInfo());
                    JSONArray contactList = resp.getJSONArray("ContactList");

                    JSONObject user = resp.getJSONObject("User");
                    info.setUserName(user.getString("UserName"));
                    info.setNickName(user.getString("NickName"));

                    for (int i = 0; i < contactList.size(); i++) {
                        JSONObject contact = contactList.getJSONObject(i);
                        Contact cnc = new Contact();
                        data.add(cnc);
                        cnc.setUserName(contact.getString("UserName"));
                        cnc.setNickName(contact.getString("NickName"));
                        cnc.setHeadImgUrl("https://wx.qq.com" + contact.getString("HeadImgUrl"));
                        cnc.setContactFlag(contact.getIntValue("ContactFlag"));
                        cnc.setpYQuanPin(contact.getString("PYQuanPin"));
                        cnc.setpYInitial(contact.getString("PYInitial"));
                        cnc.setRemarkName(contact.getString("RemarkName"));
                        cnc.setMenberCount(contact.getInteger("MemberCount"));
                        cnc.setSex(contact.getIntValue("Sex"));
                    }

                    JSONObject synckey = resp.getJSONObject("SyncKey");
                    StringBuilder synckeyStr = new StringBuilder();
                    if (synckey.getIntValue("Count") > 0) {
                        JSONArray list = synckey.getJSONArray("List");

                        for (int i = 0; i < list.size(); i++) {
                            JSONObject keyVal = list.getJSONObject(i);
                            if (synckeyStr.length() > 0) {
                                synckeyStr.append("|");
                            }
                            synckeyStr.append(keyVal.getIntValue("Key")).append("_").append(keyVal.getIntValue("Val"));
                        }

                    }

                    info.setSyncKey(synckeyStr.toString());
                }
            }
        });

        return data;
    }

    public boolean sendMesg(String content, String toUserName)
            throws Exception {
        final FinalData<Boolean> data = new FinalData<Boolean>();
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
        JSONObject message = new JSONObject();
        JSONObject baseRequest = new JSONObject();
        baseRequest.put("Uin", info.getWxuin());
        baseRequest.put("Sid", info.getWxsid());
        baseRequest.put("Skey", info.getSkey());
        baseRequest.put("DeviceID", info.getDeceiveId());

        String localId = System.currentTimeMillis() +  "" + RandomUtil.getNumber(4);
        JSONObject msg = new JSONObject();
        msg.put("Type", 1);
        msg.put("Content", content);
        msg.put("FromUserName", info.getUserName());
        msg.put("ToUserName", toUserName);
        msg.put("LocalID", localId);
        msg.put("ClientMsgId", localId);

        final Message mesg = JSONUtil.parse(msg.toJSONString(), Message.class);

        message.put("BaseRequest", baseRequest);
        message.put("Msg", msg);
        message.put("Scene", 0);

        post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?pass_ticket=" + info.getPass_ticket(), message.toJSONString(), new ResponseHandler() {
            @Override
            public void handle(Response response) throws Exception {
                JSONObject resp = JSON.parseObject(response.getStringContent());
                if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
                    WechatMessageStore.storeMessage(mesg);
                    data.setValue(true);
                }
                else {
                    data.setValue(false);
                }
            }
        });

        return data.getValue() == null ? false : data.getValue();
    }

    public boolean isHasMessage(){
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
        String queryStr = "r=" + (System.currentTimeMillis()) + "&skey=" + URLEncoder.encode(info.getSkey()) + "&sid=" +
                URLEncoder.encode(info.getWxsid()) + "&uin=" + info.getWxuin() + "&deviceid=" + info.getDeceiveId() + "&synckey=" + URLEncoder.encode(info.getSyncKey())
                + "&_=" + (System.currentTimeMillis());

        String rs = getContent("https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck?" + queryStr);
        rs = rs.replace("window.synccheck=", "");
        JSONObject response = JSON.parseObject(rs);
        if(response.getIntValue("retcode") == 0 && response.getIntValue("selector") > 0) {
            return true;
        }

        System.out.println("no content ");
        return false;

    }

    public List<Message> webwxsync()  {
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
        List<Message> mesgs = new ArrayList<Message>();

        JSONObject message = new JSONObject();
        JSONObject baseRequest = new JSONObject();
        baseRequest.put("Uin", info.getWxuin());
        baseRequest.put("Sid", info.getWxsid());
        baseRequest.put("Skey", info.getSkey());
        baseRequest.put("DeviceID", info.getDeceiveId());

        JSONObject syncKey = new JSONObject();
        JSONArray list = new JSONArray();
        String[] keyVals = info.getSyncKey().split("\\|");
        for(String keyVal : keyVals) {
            String[] kvs = keyVal.split("_");
            JSONObject keyObj = new JSONObject();
            keyObj.put("Key", Integer.parseInt(kvs[0]));
            keyObj.put("Val", Integer.parseInt(kvs[1]));
            list.add(keyObj);
        }
        syncKey.put("Count", list.size());
        syncKey.put("List", list);
        message.put("SyncKey", syncKey);
        message.put("BaseRequest", baseRequest);
        String rs = postContent("http://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid=" + info.getWxsid() + "&skey=" + info.getSkey() + "&pass_ticket="
                + info.getPass_ticket(), message.toJSONString());

        JSONObject resp = JSON.parseObject(rs);
        if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
            int mesgcount = resp.getIntValue("AddMsgCount");
            StringBuilder result = new StringBuilder();
            StringBuilder mesgStr = new StringBuilder();

            System.out.println(resp.toJSONString());

            if(mesgcount > 0) {

                JSONArray mesgList = resp.getJSONArray("AddMsgList");
                for(int i=0; i<mesgList.size(); i++) {
                    JSONObject mesg = mesgList.getJSONObject(i);
                    if(mesgStr.length() > 0) {
                        mesgStr.append("||");
                    }

                    Message msg = new Message();
                    mesgs.add(msg);
                    msg.setFromUserName(mesg.getString("FromUserName"));
                    msg.setContent(mesg.getString("Content"));
                    msg.setCreateTime(mesg.getIntValue("CreateTime"));

                    WechatMessageStore.storeMessage(msg);
                }
            }

            JSONObject synckey1 = resp.getJSONObject("SyncKey");
            final StringBuilder synckeyStr = new StringBuilder();
            if(synckey1.getIntValue("Count") > 0) {
                JSONArray list1 = synckey1.getJSONArray("List");

                for(int i=0; i<list1.size(); i++) {
                    JSONObject keyVal = list1.getJSONObject(i);
                    if(synckeyStr.length() > 0) {
                        synckeyStr.append("|");
                    }
                    synckeyStr.append(keyVal.getIntValue("Key")).append("_").append(keyVal.getIntValue("Val"));
                }

            }

            ((AuthInfo) getDefaultAuthenticationInfo().getLoginInfo()).setSyncKey(synckeyStr.toString());

        }
        else {
            System.out.println(" list content ");
        }

        return mesgs;
    }

    public String upload0(File file, String toUserName) {
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();

        JSONObject object = new JSONObject();
        object.put("UploadType", 2);
        JSONObject baseRequest = buildBaseRequest(info);
        object.put("BaseRequest", baseRequest);
        object.put("ClientMediaId", System.currentTimeMillis());
        object.put("TotalLen", file.length());
        object.put("StartPos", 0);
        object.put("DataLen", file.length());
        object.put("MediaType", 4);
        object.put("FromUserName", info.getUserName());
        object.put("ToUserName", toUserName);
        object.put("FileMd5", md5(file));

        HttpEntity entity = MultipartEntityBuilder.create()
           .setBoundary("----WebKitFormBoundaryJLAhgXlajXGZB2gI")
           .setCharset(Charset.forName("UTF-8"))
           .addBinaryBody("file", file)
           .addTextBody("id", "WU_FILE_0")
           .addTextBody("name", file.getName())
           .addTextBody("type", "image/jpeg")
           .addTextBody("lastModifiedDate", new Date().toString())
           .addTextBody("size", file.length() + "")
           .addTextBody("mediaType", "pic")
           .addTextBody("uploadmediarequest", object.toJSONString())
           .addTextBody("webwx_data_ticket", getCookie("webwx_data_ticket"))
           .addTextBody("pass_ticket", info.getPass_ticket())
           .addTextBody("filename", "filename=\"" + file.getName() + "\"")
           .build();

        try {
            String rs = upload("https://file.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json", entity);

            JSONObject resp = JSON.parseObject(rs);

            if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
                return resp.getString("MediaId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * "UploadType":2,
     "BaseRequest":{
     "Uin":1690000439,
     "Sid":"u22QR4E6mMkXwKMl",
     "Skey":"@crypt_d60d32d2_406272831fe3040f259400ae4ca33add",
     "DeviceID":"e363934621678665"
     },
     "ClientMediaId":1515866132549,
     "TotalLen":38202,
     "StartPos":0,
     "DataLen":38202,
     "MediaType":4,
     "FromUserName":"@60d9b44c63242d7ba59b4adf9d968feeed4a98861f8441710d23d9d9869d2d30",
     "ToUserName":"@672c05ad5cff16dfbc67021c2ed8c41e72c6098ef649d58ecb1fd2f85aa3c095",
     "FileMd5":"e37786af713c2e36f07fe29de71e796b"
     */
    public String upload(File file, String toUserName) {
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();

        String response = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        HttpsURLConnection conn = null;

        JSONObject object = new JSONObject();
        object.put("UploadType", 2);
        JSONObject baseRequest = buildBaseRequest(info);
        object.put("BaseRequest", baseRequest);
        object.put("ClientMediaId", System.currentTimeMillis());
        object.put("TotalLen", file.length());
        object.put("StartPos", 0);
        object.put("DataLen", file.length());
        object.put("MediaType", 4);
        object.put("FromUserName", info.getUserName());
        object.put("ToUserName", toUserName);
        object.put("FileMd5", md5(file));

        try {
            //请求头参数
            String boundary = "----WebKitFormBoundaryJLAhgXlajXGZB2gI"; //区分每个参数之间
            String newLine = "\r\n";

            URL urlObj = new URL("https://file.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json");
            conn = (HttpsURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "file.wx.qq.com");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Referer", "https://.wx.qq.com/?&lang=zh_CN");
            conn.setRequestProperty("Content-Length", Long.toString(file.length()));
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
            conn.setRequestProperty("origin", "https://.wx..qq.com/");
            conn.setRequestProperty("Connection", "Keep-Alive");

            // 请求主体
            String pre = "--";
            StringBuffer sb = new StringBuffer();

            sb.append(pre + boundary).append(newLine); //这里注意多了个freFix，来区分去请求头中的参数
            sb.append("Content-Disposition: form-data; name=\"id\"").append(newLine);
            sb.append(newLine);

            sb.append("WU_FILE_0").append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"name\"").append(newLine);
            sb.append(newLine);

            sb.append(file.getName()).append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"type\"").append(newLine);
            sb.append(newLine);

            sb.append("image/jpeg").append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"lastModifiedDate\"").append(newLine);
            sb.append(newLine);

            sb.append(new Date().toString()).append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"size\"").append(newLine);
            sb.append(newLine);

            sb.append(file.length()).append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"mediatype\"").append(newLine);
            sb.append(newLine);

            sb.append("pic").append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"uploadmediarequest\"").append(newLine);
            sb.append(newLine);

            sb.append(object.toJSONString()).append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"webwx_data_ticket\"").append(newLine);
            sb.append(newLine);

            sb.append(getCookie("webwx_data_ticket")).append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"pass_ticket\"").append(newLine);
            sb.append(newLine);

            sb.append(info.getPass_ticket()).append(newLine);
            sb.append(pre + boundary).append(newLine);
            sb.append("Content-Disposition: form-data; name=\"filename\"; filename=\""+file.getName()+"\"").append(newLine);
            sb.append("Content-Type: application/octet-stream");
            sb.append(newLine);
            sb.append(newLine);

            OutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(sb.toString().getBytes("utf-8"));//写入请求参数

            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];

            while ((bytes = dis.read(bufferOut)) != -1) {
                outputStream.write(bufferOut,0,bytes);//写入图片
            }

            outputStream.write(newLine.getBytes());
            outputStream.write((pre + boundary + newLine).getBytes("utf-8"));//标识请求数据写入结束
            dis.close();
            outputStream.close();
            //读取响应信息
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();

            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            response = buffer.toString();

            JSONObject resp = JSON.parseObject(response);

//            if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
//               return resp.getString("MediaId");
//            }
           return resp.getString("MediaId");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    "MsgID": "4243728383780954495",
"LocalID": "15159100677310122"
     */
    public String sendImage(File file, String toUserName) {
        String mediaId = upload(file, toUserName);
        AuthInfo info = (AuthInfo) getDefaultAuthenticationInfo().getLoginInfo();
        JSONObject object = new JSONObject();
        JSONObject baseRequest = buildBaseRequest(info);
        object.put("BaseRequest", baseRequest);

        JSONObject msg = new JSONObject();
        long id = System.currentTimeMillis();
        msg.put("ClientMsgId", "" + id);
        msg.put("Content", "");
        msg.put("FromUserName",info.getUserName());
        msg.put("LocalID", "" + id);
        msg.put("MediaId", mediaId);
        msg.put("ToUserName", toUserName);
        msg.put("Type", 3);

        object.put("Msg", msg);
        object.put("Scene", 0);
        String rs = postContent("https://wx.qq" +
           ".com/cgi-bin/mmwebwx-bin/webwxsendmsgimg?fun=async&f=json&lang=zh_CN&pass_ticket=" +
           info.getPass_ticket(), object.toJSONString());
        JSONObject resp = JSON.parseObject(rs);

        if(resp.getJSONObject("BaseResponse").getIntValue("Ret") == 0) {
            return mediaId + "##" + resp.getString("MsgID");
        }

        return mediaId;
    }

    public void getHistoryMessage() throws Exception {
       get("https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MjM5MjU0NTc4OQ==&scene=124&devicetype=android-24&version=26060533&lang=zh_CN&nettype=WIFI&a8scene=3&pass_ticket=gFL9EgnKicKZUAo%2BH9eLjxpPD57kfgy5g6qHgr%2Fgx3%2Fncd9PTFJWupnx%2Fvu%2BcgJH&wx_header=1", new ResponseHandler() {
          public void handle(Response response) throws Exception {
             System.out.println(response.getStringContent());
          }
       });
    }

    private JSONObject buildBaseRequest(AuthInfo info) {
        JSONObject baseRequest = new JSONObject();
        baseRequest.put("Uin", info.getWxuin());
        baseRequest.put("Sid", info.getWxsid());
        baseRequest.put("Skey", info.getSkey());
        baseRequest.put("DeviceID", info.getDeceiveId());
        return baseRequest;
    }

    public int getId() {
        return 3;
    }

    private String md5(File file) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            return  bigInt.toString(16);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        return null;

    }

    class WeChatAuthenticationInfo implements AuthenticationInfo {

        @Override
        public boolean isLogin() {
            return login;
        }

        @Override
        public void login() {
            login = true;
        }

        @Override
        public Object getLoginInfo() {
            return info;
        }

        @Override
        public void setLoginInfo(Object info) {
            Error error = (Error) info;
            this.info = new AuthInfo();
            this.info.setDeceiveId(error.getDeceiveId());
            this.info.setPass_ticket(error.getPass_ticket());
            this.info.setMessage(error.getMessage());
            this.info.setIsgrayscale(error.getIsgrayscale());
            this.info.setRet(error.getRet());
            this.info.setSkey(error.getSkey());
            this.info.setWxsid(error.getWxsid());
            this.info.setWxuin(error.getWxuin());
        }

        private AuthInfo info;
        private boolean login;
    }
}
