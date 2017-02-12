package io.github.marktony.espresso.packagedetails;

import java.util.List;

import io.github.marktony.espresso.BasePresenter;
import io.github.marktony.espresso.BaseView;
import io.github.marktony.espresso.entity.Package;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface PackageDetailsContract {

    interface View extends BaseView<Presenter> {

        void showDetailError();

        void showPackageStatus(List<Package.Data> list);

        void setCompanyName(String companyName);

        void setPackageNumber(String packageNumber);

    }

    interface Presenter extends BasePresenter {

    }

}
