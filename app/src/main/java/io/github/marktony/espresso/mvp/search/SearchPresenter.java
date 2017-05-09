/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    private String queryWords = null;

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
        search(queryWords);
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void search(String keyWords) {

        if (keyWords == null || keyWords.isEmpty()) {
            view.showResult(null, null);
            return;
        }
        queryWords = keyWords;

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
