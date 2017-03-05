package io.github.marktony.espresso.mvp.companies;

import android.support.annotation.NonNull;

import io.github.marktony.espresso.data.CompanyList;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    public void getCompanies() {
        RetrofitClient.getInstance().create(RetrofitService.class)
                .getCompanies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompanyList value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showGetCompaniesError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
