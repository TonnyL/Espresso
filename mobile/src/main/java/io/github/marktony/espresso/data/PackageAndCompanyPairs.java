package io.github.marktony.espresso.data;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/3/27.
 */

public class PackageAndCompanyPairs {

    private List<Package> packages;
    private List<Company> companies;

    public PackageAndCompanyPairs(List<Package> packages, List<Company> companies) {
        this.packages = packages;
        this.companies = companies;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
