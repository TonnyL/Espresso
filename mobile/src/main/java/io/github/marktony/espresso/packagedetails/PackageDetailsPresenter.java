package io.github.marktony.espresso.packagedetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.marktony.espresso.constant.API;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackageDetailsPresenter implements PackageDetailsContract.Presenter {

    @NonNull
    private PackageDetailsContract.View view;

    @NonNull
    private PackagesRepository packagesRepository;

    @NonNull
    private CompositeDisposable disposable;

    @Nullable
    private String packageId;

    public PackageDetailsPresenter(@Nullable String packageId,
                                   @NonNull PackagesRepository packagesRepository,
                                   @NonNull PackageDetailsContract.View packageDetailView) {
        this.packageId = packageId;
        this.view = packageDetailView;

        disposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        openDetail();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    private void openDetail() {
        if (packageId == null || packageId.isEmpty()) {
            view.showDetailError();
            return;
        }

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
                        view.showPackageStatus(value.getData());
                        view.setPackageNumber(value.getNumber());
                        view.setCompanyName(value.getCom());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
