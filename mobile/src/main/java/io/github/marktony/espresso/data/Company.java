package io.github.marktony.espresso.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by lizhaotailang on 2017/2/23.
 * Immutable model class for a Company.
 * JSON格式示例
 * JSON format sample
 * {
 * 'cid':'9',
 * 'id':'2',
 * 'companyname':'EMS',
 * shortname':'EMS',
 * 'tel':'11183',
 * 'url':'ems',
 * 'code':'ems',
 * 'hasvali':0,
 * 'comurl':'http://www.ems.com.cn/',
 * 'isavailable':'0',
 * 'promptinfo':'',
 * 'testnu':'5116741128399',
 * 'freg':'',
 * 'freginfo':'13位纯数字或者开头和结尾各2位字母中间9位数字',
 * 'telcomplaintnum':'11183',
 * 'queryurl':'http://www.11183.com.cn/ems/order/singleQuery_t?mailNum=',
 * 'serversite':''
 * }
 */

public class Company extends RealmObject {

    @Expose
    @SerializedName("cid")
    private String cid;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("companyname")
    private String companyName;
    @Expose
    @SerializedName("shortname")
    private String shortName;
    @Expose
    @SerializedName("tel")
    private String tel;
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("code")
    private String code;
    @Expose
    @SerializedName("hasvali")
    private String hasVali;
    @Expose
    @SerializedName("comurl")
    private String comUrl;
    @Expose
    @SerializedName("isavailable")
    private String isAvailable;
    @Expose
    @SerializedName("promptinfo")
    private String promptInfo;
    @Expose
    @SerializedName("testnu")
    private String testNu;
    @Expose
    @SerializedName("freg")
    private String freg;
    @Expose
    @SerializedName("freginfo")
    private String fregInfo;
    @Expose
    @SerializedName("telcomplaintnum")
    private String telComplaintNum;
    @Expose
    @SerializedName("queryurl")
    private String queryUrl;
    @Expose
    @SerializedName("serversite")
    private String serverSite;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHasVali() {
        return hasVali;
    }

    public void setHasVali(String hasVali) {
        this.hasVali = hasVali;
    }

    public String getComUrl() {
        return comUrl;
    }

    public void setComUrl(String comUrl) {
        this.comUrl = comUrl;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getPromptInfo() {
        return promptInfo;
    }

    public void setPromptInfo(String promptInfo) {
        this.promptInfo = promptInfo;
    }

    public String getTestNu() {
        return testNu;
    }

    public void setTestNu(String testNu) {
        this.testNu = testNu;
    }

    public String getFreg() {
        return freg;
    }

    public void setFreg(String freg) {
        this.freg = freg;
    }

    public String getFregInfo() {
        return fregInfo;
    }

    public void setFregInfo(String fregInfo) {
        this.fregInfo = fregInfo;
    }

    public String getTelComplaintNum() {
        return telComplaintNum;
    }

    public void setTelComplaintNum(String telComplaintNum) {
        this.telComplaintNum = telComplaintNum;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getServerSite() {
        return serverSite;
    }

    public void setServerSite(String serverSite) {
        this.serverSite = serverSite;
    }
}
