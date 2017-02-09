package io.github.marktony.espresso.interfaze;

import io.github.marktony.espresso.constant.API;
import io.github.marktony.espresso.entity.CompanyAuto;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lizhaotailang on 2017/2/9.
 */

public interface CompanyQuery {

    @GET(API.TEST)
    Observable<CompanyAuto> query(@Query("text") String number);

}
