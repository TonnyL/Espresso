package io.github.marktony.espresso.retrofit;

import io.github.marktony.espresso.constant.Api;
import io.github.marktony.espresso.data.CompanyRecognition;
import io.github.marktony.espresso.data.CompanyList;
import io.github.marktony.espresso.data.Package;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lizhaotailang on 2017/2/9.
 */

public interface RetrofitService {

    @GET(Api.COMPANY_QUERY)
    Observable<CompanyRecognition> query(@Query("text") String number);

    @GET(Api.PACKAGE_STATE)
    Observable<Package> getPackageState(@Query("type") String type, @Query("postid") String postId);

    @GET(Api.COMPANIES)
    Observable<CompanyList> getCompanies();

}
