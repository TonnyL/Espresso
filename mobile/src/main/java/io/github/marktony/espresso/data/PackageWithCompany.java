package io.github.marktony.espresso.data;

/**
 * Created by lizhaotailang on 2017/3/25.
 */

public class PackageWithCompany {

    private Package pkg;
    private Company company;

    public PackageWithCompany(Package p, Company c) {
        this.pkg = p;
        this.company = c;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
