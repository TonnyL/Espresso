package io.github.marktony.espresso.packages;

import java.util.List;

import io.github.marktony.espresso.BasePresenter;
import io.github.marktony.espresso.BaseView;
import io.github.marktony.espresso.entity.Package;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface PackagesContract {

    interface View extends BaseView<Presenter> {

        // 显示正在加载或者停止加载
        // show it is loading or stop loading
        void setProgressIndicator(boolean active);

        // if it is unable to get the states,
        // show this error
        void showLoadingPackageStatesError();

        void showPackageStates(List<Package> list);

    }

    interface Presenter extends BasePresenter {

    }

}
