package io.github.marktony.espresso.mvp.packages;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.BasePresenter;
import io.github.marktony.espresso.BaseView;
import io.github.marktony.espresso.data.Package;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface PackagesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showEmptyView(boolean toShow);

        void showPackages(@NonNull List<Package> list);

        void shareTo(@NonNull Package pack);

        void showPackageRemovedMsg(String packageName);

    }

    interface Presenter extends BasePresenter {

        void loadPackages(boolean forceUpdate);

        void openPackageDetails(@NonNull Package pack);

        void markAllPacksRead();

        void setFiltering(@NonNull PackageFilterType requestType);

        PackageFilterType getFiltering();

        void setPackageReadUnread(@NonNull String packageId);

        void deletePackage(int position);

        void setShareData(@NonNull String packageId);

        void recoverPackage();

        void refreshPackages();

    }

}
