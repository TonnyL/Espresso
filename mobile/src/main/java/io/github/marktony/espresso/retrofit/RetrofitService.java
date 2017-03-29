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

package io.github.marktony.espresso.retrofit;

import io.github.marktony.espresso.data.CompanyRecognition;
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

}
