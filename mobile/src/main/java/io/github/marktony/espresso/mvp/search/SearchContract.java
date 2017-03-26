package io.github.marktony.espresso.mvp.search;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.mvp.BasePresenter;
import io.github.marktony.espresso.mvp.BaseView;

/**
 * Created by lizhaotailang on 2017/3/26.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showResult(List<Package> packages, List<Company> companies);

    }

    interface Presenter extends BasePresenter {

        void search(String keyWords);

    }

}
