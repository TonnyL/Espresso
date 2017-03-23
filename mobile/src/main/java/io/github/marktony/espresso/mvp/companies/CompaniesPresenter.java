package io.github.marktony.espresso.mvp.companies;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.source.CompaniesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompaniesPresenter implements CompaniesContract.Presenter {

    private static final String TAG = CompaniesPresenter.class.getSimpleName();

    @NonNull
    private CompaniesContract.View view;

    @NonNull
    private CompaniesRepository companiesRepository;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public CompaniesPresenter(@NonNull CompaniesContract.View view,
                              @NonNull CompaniesRepository companiesRepository) {
        this.view = view;
        this.companiesRepository = companiesRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getCompanies();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void getCompanies() {
        Disposable disposable = companiesRepository
                .getCompanies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Company>>() {
                    @Override
                    public void onNext(List<Company> value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

}
