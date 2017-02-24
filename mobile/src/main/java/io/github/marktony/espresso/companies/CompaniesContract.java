package io.github.marktony.espresso.companies;

import java.util.ArrayList;

import io.github.marktony.espresso.BasePresenter;
import io.github.marktony.espresso.BaseView;
import io.github.marktony.espresso.entity.Company;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface CompaniesContract {

    interface View extends BaseView<Presenter> {

        void showGetCompaniesError();

        void showCompanies(ArrayList<Company> list);

    }

    interface Presenter extends BasePresenter {

    }

}
