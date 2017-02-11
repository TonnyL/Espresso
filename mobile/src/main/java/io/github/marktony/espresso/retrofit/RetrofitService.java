package io.github.marktony.espresso.retrofit;

import io.github.marktony.espresso.constant.API;
import io.github.marktony.espresso.entity.CompanyAuto;
import io.github.marktony.espresso.entity.PackageState;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lizhaotailang on 2017/2/9.
 */

public interface RetrofitService {

    @GET(API.COMPANY_QUERY)
    Observable<CompanyAuto> query(@Query("text") String number);

    @GET(API.PACKAGE_STATE)
    Observable<PackageState> getPakageState(@Query("type") String type, @Query("postid") String postId);

}
