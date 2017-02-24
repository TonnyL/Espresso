package io.github.marktony.espresso.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by lizhaotailang on 2017/2/24.
 */
public class PackageStatus extends RealmObject {

    @Expose
    @SerializedName("time")
    private String time;
    @Expose
    @SerializedName("ftime")
    private String ftime;
    @Expose
    @SerializedName("context")
    private String context;
    @Expose
    @SerializedName("location")
    private String location;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
