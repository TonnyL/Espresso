/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lizhaotailang on 2017/2/10.
 * Immutable model class for a Package.
 * 示例JSON格式
 * JSON format sample
 * {
 * "message":"ok",
 * "nu":"47258833029",
 * "ischeck":"1",
 * "condition":"F00",
 * "com":"jd",
 * "status":"200",
 * "state":"3",
 * "data":
 * [
 * {
 * "time":"2016-12-13 21:35:51",
 * "ftime":"2016-12-13 21:35:51",
 * "context":"货物已完成配送，感谢您选择京东配送",
 * "location":""
 * },
 * {
 * "time":"2016-12-13 11:01:55",
 * "ftime":"2016-12-13 11:01:55",
 * "context":"配送员开始配送，请您准备收货，配送员，朱小宝，手机号，13060495388或0917-2622505",
 * "location":""
 * },
 * {
 * "time":"2016-12-13 10:06:25",
 * "ftime":"2016-12-13 10:06:25",
 * "context":"货物已分配，等待配送",
 * "location":""
 * },
 * {
 * "time":"2016-12-13 10:06:22",
 * "ftime":"2016-12-13 10:06:22",
 * "context":"货物已到达【宝鸡北环站】","location":""
 * },
 * ...
 * ]
 * }
 */

public class Package extends RealmObject {

    public static final int STATUS_FAILED = 2, STATUS_NORMAL = 0,
                            STATUS_ON_THE_WAY = 5, STATUS_DELIVERED = 3,
                            STATUS_RETURNED = 4, STATUS_RETURNING = 6,
                            STATUS_OTHER = 1;

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("nu")
    @PrimaryKey
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
    private RealmList<PackageStatus> data;

    @Expose
    private boolean pushable = false;
    @Expose
    private boolean readable = false;
    @Expose
    private String name;
    @Expose
    private String companyChineseName;
    @Expose
    private int colorAvatar;
    @Expose
    private long timestamp;

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

    public RealmList<PackageStatus> getData() {
        return data;
    }

    public void setData(RealmList<PackageStatus> data) {
        this.data = data;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isPushable() {
        return pushable;
    }

    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyChineseName() {
        return companyChineseName;
    }

    public void setCompanyChineseName(String companyChineseName) {
        this.companyChineseName = companyChineseName;
    }

    public void setColorAvatar(int colorAvatar) {
        this.colorAvatar = colorAvatar;
    }

    public int getColorAvatar() {
        return colorAvatar;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
