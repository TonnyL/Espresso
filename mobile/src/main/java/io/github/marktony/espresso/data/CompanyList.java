package io.github.marktony.espresso.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

/**
 * Created by lizhaotailang on 2017/2/22.
 * Immutable model class for a PackageList.
 */

public class CompanyList {

    @Expose
    @SerializedName("company_name")
    private RealmList<Company> companies;
    @Expose
    @SerializedName("error_size")
    private int errorSize;

    public RealmList<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(RealmList<Company> companies) {
        this.companies = companies;
    }

    public int getErrorSize() {
        return errorSize;
    }

    public void setErrorSize(int errorSize) {
        this.errorSize = errorSize;
    }

}
