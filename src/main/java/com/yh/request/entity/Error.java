package com.yh.request.entity;

public class Error {


    //	<error>
//		<ret>0</ret>
//		<message></message>
//		<skey>@crypt_d60d32d2_fdac679cb62fd327df9abd0ca97845ce</skey>
//		<wxsid>ptipIuAcnOU+rxqZ</wxsid>
//		<wxuin>1690000439</wxuin>
//		<pass_ticket>5I6r9cgB4gsMmak%2FX4fAZCrHe%2BpKhMUPKQvPpS3%2BpQ0%2FiT3Rd%2F10vW4zoa5SgTVH</pass_ticket>
//		<isgrayscale>1</isgrayscale>
//	</error>
    private String message;
    private String skey;
    private String wxsid;
    private int wxuin;
    private String pass_ticket;
    private int isgrayscale;
    private int ret;
    private String deceiveId;
    private String syncKey;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSkey() {
        return skey;
    }
    public void setSkey(String skey) {
        this.skey = skey;
    }
    public String getWxsid() {
        return wxsid;
    }
    public void setWxsid(String wxsid) {
        this.wxsid = wxsid;
    }
    public int getWxuin() {
        return wxuin;
    }
    public void setWxuin(int wxuin) {
        this.wxuin = wxuin;
    }
    public String getPass_ticket() {
        return pass_ticket;
    }
    public void setPass_ticket(String pass_ticket) {
        this.pass_ticket = pass_ticket;
    }
    public int getIsgrayscale() {
        return isgrayscale;
    }
    public void setIsgrayscale(int isgrayscale) {
        this.isgrayscale = isgrayscale;
    }
    public int getRet() {
        return ret;
    }
    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getDeceiveId() {
        return deceiveId;
    }

    public void setDeceiveId(String deceiveId) {
        this.deceiveId = deceiveId;
    }

    public String getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(String syncKey) {
        this.syncKey = syncKey;
    }
}
