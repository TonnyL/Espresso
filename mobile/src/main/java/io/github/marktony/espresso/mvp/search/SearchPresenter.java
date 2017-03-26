package io.github.marktony.espresso.mvp.search;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.PackageAndCompanyPairs;
import io.github.marktony.espresso.data.source.CompaniesRepository;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizhaotailang on 2017/3/26.
 */

public class SearchPresenter implements SearchContract.Presenter{

    @NonNull
    private SearchContract.View view;

    @NonNull
    private PackagesRepository packagesRepository;

    @NonNull
    private CompaniesRepository companiesRepository;

    private CompositeDisposable compositeDisposable;

    public SearchPresenter(@NonNull SearchContract.View view,
                           @NonNull PackagesRepository packagesRepository,
                           @NonNull CompaniesRepository companiesRepository) {
        this.view = view;
        this.packagesRepository = packagesRepository;
        this.companiesRepository = companiesRepository;
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

    @Override
    public void search(String keyWords) {

        Observable<List<Company>> companyObservable = companiesRepository
                .searchCompanies(keyWords)
                .subscribeOn(Schedulers.io());

        Observable<List<Package>> packageObservable = packagesRepository
                .searchPackages(keyWords)
                .subscribeOn(Schedulers.io());

        Disposable disposable = Observable
                .zip(packageObservable, companyObservable, new BiFunction<List<Package>, List<Company>, PackageAndCompanyPairs>() {
                    @Override
                    public PackageAndCompanyPairs apply(List<Package> packages, List<Company> companies) throws Exception {
                        return new PackageAndCompanyPairs(packages, companies);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PackageAndCompanyPairs>() {
                    @Override
                    public void onNext(PackageAndCompanyPairs value) {
                        view.showResult(value.getPackages(), value.getCompanies());
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
