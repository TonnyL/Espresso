package io.github.marktony.espresso.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.reactivex.Observable;

/**
 * Created by lizhaotailang on 2017/3/22.
 * Main entry point for accessing companies data.
 * <p/>
 */

public interface CompaniesDataSource {

    Observable<List<Company>> getCompanies();

    Observable<Company> getCompany(@NonNull String companyId);

    void initData();

    Observable<List<Company>> searchCompanies(@NonNull String keyWords);

}
