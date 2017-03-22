package io.github.marktony.espresso.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.source.CompaniesDataSource;
import io.github.marktony.espresso.realm.RealmHelper;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

/**
 * Created by lizhaotailang on 2017/3/22.
 */

public class CompaniesLocalDataSource implements CompaniesDataSource {

    @Nullable
    public static CompaniesLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private CompaniesLocalDataSource() {

    }

    public static CompaniesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CompaniesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Company>> getCompanies() {
        Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
        return Observable
                .fromIterable(rlm.copyFromRealm(rlm.where(Company.class).findAllSorted("index", Sort.ASCENDING)))
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Company> getCompany(@NonNull String companyId) {
        Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
        return Observable
                .just(rlm.copyFromRealm(rlm.where(Company.class).equalTo("id", companyId).findFirst()));
    }

    @Override
    public void initData() {
        Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
        rlm.beginTransaction();
        rlm.createOrUpdateObjectFromJson(Company.class, "{‘companyname’:'顺丰速运','id':'shunfeng','tel':'95338','website':'http://www.sf-express.com','index':'shunfengsuyun'}");

        rlm.commitTransaction();
        rlm.close();
    }

}
