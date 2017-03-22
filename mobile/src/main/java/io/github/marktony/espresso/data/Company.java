package io.github.marktony.espresso.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lizhaotailang on 2017/2/23.
 * Immutable model class for a Company.
 * JSON format sample
 * {
 * ‘companyname’:'EMS',
 * 'id':'ems',
 * 'tel':'11183',
 * 'website':'http://www.ems.com.cn/',
 * 'index':'ems'
 * }
 *
 */

public class Company extends RealmObject {

    @Expose
    @SerializedName("companyname")
    private String chineseName;

    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("tel")
    private String tel;

    @Expose
    @SerializedName("website")
    private String website;

    @Expose
    @SerializedName("index")
    private String index;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
