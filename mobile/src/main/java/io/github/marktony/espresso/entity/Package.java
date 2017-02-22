package io.github.marktony.espresso.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class Package {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("nu")
    private String number;
    @Expose
    @SerializedName("ischeck")
    private String isCheck;
    @Expose
    @SerializedName("condition")
    private String condition;
    @Expose
    @SerializedName("com")
    private String company;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("state")
    private String state;
    @Expose
    @SerializedName("data")
    private List<Data> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCom() {
        return company;
    }

    public void setCom(String company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
