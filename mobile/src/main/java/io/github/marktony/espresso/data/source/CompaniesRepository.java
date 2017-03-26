package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.reactivex.Observable;

/**
 * Created by lizhaotailang on 2017/3/22.
 */

public class CompaniesRepository implements CompaniesDataSource {

    @Nullable
    private static CompaniesRepository INSTANCE = null;

    @NonNull
    private final CompaniesDataSource localDataSource;

    // Prevent direct instantiation
    private CompaniesRepository(@NonNull CompaniesDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static CompaniesRepository getInstance(@NonNull CompaniesDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CompaniesRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Company>> getCompanies() {
        return localDataSource.getCompanies();
    }

    @Override
    public Observable<Company> getCompany(@NonNull String companyId) {
        return localDataSource.getCompany(companyId);
    }

    @Override
    public void initData() {
        localDataSource.initData();
    }

    @Override
    public Observable<List<Company>> searchCompanies(@NonNull String keyWords) {
        return localDataSource.searchCompanies(keyWords);
    }

}
