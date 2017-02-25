package io.github.marktony.espresso.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/2/9.
 */

public class CompanyAuto {

    @Expose
    @SerializedName("companyCode")
    private String companyCode;
    @Expose
    @SerializedName("num")
    private String number;
    @Expose
    @SerializedName("auto")
    private List<Auto> auto;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Auto> getAuto() {
        return auto;
    }

    public void setAuto(List<Auto> auto) {
        this.auto = auto;
    }

    public class Auto {

        @Expose
        @SerializedName("comCode")
        private String companyCode;
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("noCount")
        private int noCount;
        @Expose
        @SerializedName("noPre")
        private String noPre;
        @Expose
        @SerializedName("startTime")
        private String startTime;

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNoCount() {
            return noCount;
        }

        public void setNoCount(int noCount) {
            this.noCount = noCount;
        }

        public String getNoPre() {
            return noPre;
        }

        public void setNoPre(String noPre) {
            this.noPre = noPre;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
