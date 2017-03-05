package io.github.marktony.espresso.mvp.companydetails;

import android.support.annotation.NonNull;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompanyDetailPresenter implements CompanyDetailContract.Presenter {

    private CompanyDetailContract.View view;

    public CompanyDetailPresenter(@NonNull CompanyDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}
