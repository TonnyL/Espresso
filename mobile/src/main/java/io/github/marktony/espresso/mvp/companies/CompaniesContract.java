package io.github.marktony.espresso.mvp.companies;

import java.util.ArrayList;
import java.util.List;

import io.github.marktony.espresso.mvp.BasePresenter;
import io.github.marktony.espresso.mvp.BaseView;
import io.github.marktony.espresso.data.Company;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface CompaniesContract {

    interface View extends BaseView<Presenter> {

        void showGetCompaniesError();

        void showCompanies(List<Company> list);

    }

    interface Presenter extends BasePresenter {

    }

}
