package io.github.marktony.espresso.packages;

import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.github.marktony.espresso.constant.API;
import io.github.marktony.espresso.entity.Package;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackagesPresenter implements PackagesContract.Presenter {

    @NonNull
    private final PackagesContract.View view;

    @NonNull
    private final CompositeDisposable disposable;

    public PackagesPresenter(@NonNull PackagesContract.View view) {
        this.view = view;

        disposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadPackageStates();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    private void loadPackageStates() {

        /*view.setProgressIndicator(true);

        // just for test
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.API_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        service.getPackageState("jd", API.TEST_NUMBER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Package>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Package value) {
                        List<Package> list = new ArrayList<Package>();
                        list.add(value);
                        view.showPackageStates(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showLoadingPackageStatesError();
                        view.setProgressIndicator(false);
                    }

                    @Override
                    public void onComplete() {
                        view.setProgressIndicator(false);
                    }
                });*/

    }

}
