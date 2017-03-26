package io.github.marktony.espresso.mvp.companydetails;

import java.util.List;

import io.github.marktony.espresso.mvp.BasePresenter;
import io.github.marktony.espresso.mvp.BaseView;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public interface CompanyDetailContract {

    interface View extends BaseView<Presenter> {

        void setCompanyName(String name);

        void setCompanyTel(String tel);

        void setCompanyWebsite(String website);

        void showErrorMsg();



    }

    interface Presenter extends BasePresenter {

    }

}
