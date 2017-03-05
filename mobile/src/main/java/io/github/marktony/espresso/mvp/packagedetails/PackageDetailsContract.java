package io.github.marktony.espresso.mvp.packagedetails;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.BasePresenter;
import io.github.marktony.espresso.BaseView;
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

        void setPackageReadUnread(boolean readUnread);

    }

    interface Presenter extends BasePresenter {

        void setPackageReadUnread(@NonNull String packageId);

    }

}
