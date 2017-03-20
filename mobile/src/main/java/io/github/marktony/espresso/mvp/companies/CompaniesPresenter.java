package io.github.marktony.espresso.mvp.companies;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompaniesPresenter implements CompaniesContract.Presenter {

    private CompaniesContract.View view;

    private CompositeDisposable compositeDisposable;

    public CompaniesPresenter(@NonNull CompaniesContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        getCompanies();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    public void getCompanies() {

    }

}
