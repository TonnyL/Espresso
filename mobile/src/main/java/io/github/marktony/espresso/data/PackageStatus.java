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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;

/**
 * Created by lizhaotailang on 2017/2/24.
 * Immutable model class for a PackageStatus.
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

    @Expose
    private String phone;

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

    // Storing the phone number to database
    // could reduce the computation when the function is called.
    public String getPhone() {
        if (phone != null) {
            return phone;
        }
        return findPhoneNumber(getContext());
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String findPhoneNumber(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        // Chinese cell phone number contains 11 numbers in total,
        String chinesePhone = "(?<!\\d)(?:(?:1[3578]\\d{9})|(?:861[3578]\\d{9}))(?!\\d)";
        // HK cell phone number has 8 numbers
        // Begin with 5 or 6 or 8 or 9 + 7 arbitrary numbers
        // String hkPhone = "^(5|6|8|9)\\\\d{7}$";
        // Pattern hkPattern = Pattern.compile(hkPhone);

        Pattern cnPattern = Pattern.compile(chinesePhone);
        Matcher matcher = cnPattern.matcher(s);
        StringBuilder buffer = new StringBuilder(64);
        while (matcher.find()) {
            buffer.append(matcher.group()).append("~");
        }
        int length = buffer.length();
        if (length > 0) {
            buffer.deleteCharAt(length - 1);
        }
        String number = buffer.toString();
        if (number.length() < 8) {
            return null;
        }
        if (number.contains("~")) {
            number = number.substring(0, number.indexOf("~"));
        }
        return number;
    }

}
