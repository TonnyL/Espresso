package io.github.marktony.espresso.mvp.companydetails;

import android.support.annotation.NonNull;

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

public class CompanyDetailPresenter implements CompanyDetailContract.Presenter {

    @NonNull
    private CompanyDetailContract.View view;

    @NonNull
    private CompaniesRepository companiesRepository;

    @NonNull
    private String companyId;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public CompanyDetailPresenter(@NonNull CompanyDetailContract.View view,
                                  @NonNull CompaniesRepository companiesRepository,
                                  @NonNull String companyId) {
        this.view = view;
        this.companiesRepository = companiesRepository;
        this.companyId = companyId;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        fetchCompanyData();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void fetchCompanyData() {
        Disposable disposable = companiesRepository
                .getCompany(companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Company>() {
                    @Override
                    public void onNext(Company value) {
                        if (value != null) {
                            view.setCompanyName(value.getName());
                            view.setCompanyTel(value.getTel());
                            view.setCompanyWebsite(value.getWebsite());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMsg();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

}
