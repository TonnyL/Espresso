package io.github.marktony.espresso.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/2/22.
 */

public class CompanyList {

    @Expose
    @SerializedName("company")
    private List<Company> companies;
    @Expose
    @SerializedName("error_size")
    private int errorSize;

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public int getErrorSize() {
        return errorSize;
    }

    public void setErrorSize(int errorSize) {
        this.errorSize = errorSize;
    }

}
