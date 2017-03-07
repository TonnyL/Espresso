package io.github.marktony.espresso.mvp.packagedetails;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.mvp.BasePresenter;
import io.github.marktony.espresso.mvp.BaseView;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.PackageStatus;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface PackageDetailsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean loading);

        void showPackageStatus(@NonNull List<PackageStatus> list);

        void setCompanyName(@NonNull String companyName);

        void setPackageNumber(@NonNull String packageNumber);

        void setPackageName(@NonNull String name);

        void setToolbarBackground(@DrawableRes int resId);

        void setPackageUnread(@NonNull String packageId, int position);

        void shareTo(@NonNull Package pack);

        void deletePackage(@NonNull String packageId, int position);

        void copyPackageNumber(@NonNull String packageId);
    }

    interface Presenter extends BasePresenter {

        void setPackageReadable();

        void refreshPackage();

        void deletePackage();

        void copyPackageNumber();

        void shareTo();



    }

}
