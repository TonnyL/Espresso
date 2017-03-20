package io.github.marktony.espresso.mvp.addpackage;

import io.github.marktony.espresso.mvp.BasePresenter;
import io.github.marktony.espresso.mvp.BaseView;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface AddPackageContract {

    interface View extends BaseView<Presenter> {

        void showNumberExistError();

        void showNumberError();

        void setProgressIndicator(boolean loading);

        void showPackagesList();

        void showNetworkError();

    }

    interface Presenter extends BasePresenter {

        void savePackage(String number, String name, int color);

    }

}
